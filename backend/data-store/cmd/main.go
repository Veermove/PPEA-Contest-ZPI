package main

import (
	"context"
	"flag"
	"log"
	"net"
	"os"
	"os/signal"
	"syscall"

	dbclient "zpi/db-client"

	store "zpi/pb"

	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"google.golang.org/grpc"
)

var BuildDate string

func main() {
	var (
		debug                bool
		init_dictionary_data bool
	)
	flag.BoolVar(&debug, "debug", false, "debug mode")
	flag.BoolVar(&init_dictionary_data, "initdict", false, "initialize dictionary data")
	flag.Parse()

	var (
		l           = GetLogger(debug)
		ctx, cancel = context.WithCancel(context.Background())
		server      = grpc.NewServer()
		exitStatus  int
	)

	l.Info("hello", zap.String("built on", BuildDate))
	l.Debug("debug mode enabled")

	sdb, err := dbclient.Open(ctx, l, init_dictionary_data)
	if err != nil {
		l.Fatal("opening db", zap.Error(err))
	}

	err = sdb.Pool.Ping(ctx)
	if err != nil {
		l.Fatal("pinging db", zap.Error(err))
	}

	store.RegisterDataStoreServer(server, &DataStore{
		Log: l,
		Db:  sdb,
	})

	lis, err := net.Listen("tcp4", ":8080")

	if err != nil {
		l.Fatal("opening socket", zap.Error(err))
	}

	l.Info("serving", zap.String("addr", lis.Addr().String()))

	c := make(chan os.Signal, 1)
	signal.Notify(c, syscall.SIGTERM)
	signal.Notify(c, syscall.SIGINT)

	go func() {
		<-c
		l.Info("got signal SIGTERM")
		l.Info("graceful shutdown starting")
		cancel()

		server.GracefulStop()
		sdb.Close()
		l.Info("graceful shutdown complete. Bye")
	}()

	if err := server.Serve(lis); err != nil {
		l.Fatal("serving grpc", zap.Error(err))
	}

	os.Exit(exitStatus)
}

func GetLogger(debug bool) *zap.Logger {

	config := &zap.Config{
		Level:            zap.NewAtomicLevelAt(zapcore.InfoLevel),
		Encoding:         "json",
		EncoderConfig:    zap.NewProductionEncoderConfig(),
		OutputPaths:      []string{"stdout"},
		ErrorOutputPaths: []string{"stderr"},
		DisableCaller:    true,
	}

	if debug {
		config.EncoderConfig = zap.NewDevelopmentEncoderConfig()
		config.Level = zap.NewAtomicLevelAt(zapcore.DebugLevel)
		config.Encoding = "console"
	}

	l, err := config.Build()
	if err != nil {
		log.Fatal("failed to initialize logger", zap.Error(err))
	}

	zap.ReplaceGlobals(l)

	return l
}

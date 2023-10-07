package envvar

import (
	_ "embed"
	"strings"
)

//go:embed .env
var vars string

var EnvVar map[string]string

func init() {
	EnvVar = make(map[string]string)
	for _, line := range strings.Split(vars, "\n") {
		if len(line) == 0 {
			continue
		}
		kv := strings.Split(line, "=")
		EnvVar[kv[0]] = kv[1]
	}
}

// GetOrPanic returns the value of the environment variable
// or panics if the variable is not set.
// This should only during initialization, not during runtime.
func GetOrPanic(key string) string {
	val, ok := EnvVar[key]
	if !ok {
		panic("env var not set: " + key)
	}
	return val
}

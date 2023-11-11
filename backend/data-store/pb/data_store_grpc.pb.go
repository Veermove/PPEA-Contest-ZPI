// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.2.0
// - protoc             v3.6.1
// source: data_store.proto

package data_store

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// DataStoreClient is the client API for DataStore service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type DataStoreClient interface {
	GetUserClaims(ctx context.Context, in *UserRequest, opts ...grpc.CallOption) (*UserClaimsResponse, error)
	GetSubmissions(ctx context.Context, in *SubmissionRequest, opts ...grpc.CallOption) (*SubmissionsResponse, error)
	GetSubmissionDetails(ctx context.Context, in *DetailsSubmissionRequest, opts ...grpc.CallOption) (*DetailsSubmissionResponse, error)
	GetSubmissionRatings(ctx context.Context, in *RatingsSubmissionRequest, opts ...grpc.CallOption) (*RatingsSubmissionResponse, error)
	PostNewSubmissionRating(ctx context.Context, in *NewSubmissionRatingRequest, opts ...grpc.CallOption) (*Rating, error)
	PostPartialRating(ctx context.Context, in *PartialRatingRequest, opts ...grpc.CallOption) (*PartialRating, error)
	PostSubmitRating(ctx context.Context, in *SubmitRatingDraft, opts ...grpc.CallOption) (*Rating, error)
}

type dataStoreClient struct {
	cc grpc.ClientConnInterface
}

func NewDataStoreClient(cc grpc.ClientConnInterface) DataStoreClient {
	return &dataStoreClient{cc}
}

func (c *dataStoreClient) GetUserClaims(ctx context.Context, in *UserRequest, opts ...grpc.CallOption) (*UserClaimsResponse, error) {
	out := new(UserClaimsResponse)
	err := c.cc.Invoke(ctx, "/data_store.DataStore/GetUserClaims", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *dataStoreClient) GetSubmissions(ctx context.Context, in *SubmissionRequest, opts ...grpc.CallOption) (*SubmissionsResponse, error) {
	out := new(SubmissionsResponse)
	err := c.cc.Invoke(ctx, "/data_store.DataStore/GetSubmissions", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *dataStoreClient) GetSubmissionDetails(ctx context.Context, in *DetailsSubmissionRequest, opts ...grpc.CallOption) (*DetailsSubmissionResponse, error) {
	out := new(DetailsSubmissionResponse)
	err := c.cc.Invoke(ctx, "/data_store.DataStore/GetSubmissionDetails", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *dataStoreClient) GetSubmissionRatings(ctx context.Context, in *RatingsSubmissionRequest, opts ...grpc.CallOption) (*RatingsSubmissionResponse, error) {
	out := new(RatingsSubmissionResponse)
	err := c.cc.Invoke(ctx, "/data_store.DataStore/GetSubmissionRatings", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *dataStoreClient) PostNewSubmissionRating(ctx context.Context, in *NewSubmissionRatingRequest, opts ...grpc.CallOption) (*Rating, error) {
	out := new(Rating)
	err := c.cc.Invoke(ctx, "/data_store.DataStore/PostNewSubmissionRating", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *dataStoreClient) PostPartialRating(ctx context.Context, in *PartialRatingRequest, opts ...grpc.CallOption) (*PartialRating, error) {
	out := new(PartialRating)
	err := c.cc.Invoke(ctx, "/data_store.DataStore/PostPartialRating", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *dataStoreClient) PostSubmitRating(ctx context.Context, in *SubmitRatingDraft, opts ...grpc.CallOption) (*Rating, error) {
	out := new(Rating)
	err := c.cc.Invoke(ctx, "/data_store.DataStore/PostSubmitRating", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// DataStoreServer is the server API for DataStore service.
// All implementations must embed UnimplementedDataStoreServer
// for forward compatibility
type DataStoreServer interface {
	GetUserClaims(context.Context, *UserRequest) (*UserClaimsResponse, error)
	GetSubmissions(context.Context, *SubmissionRequest) (*SubmissionsResponse, error)
	GetSubmissionDetails(context.Context, *DetailsSubmissionRequest) (*DetailsSubmissionResponse, error)
	GetSubmissionRatings(context.Context, *RatingsSubmissionRequest) (*RatingsSubmissionResponse, error)
	PostNewSubmissionRating(context.Context, *NewSubmissionRatingRequest) (*Rating, error)
	PostPartialRating(context.Context, *PartialRatingRequest) (*PartialRating, error)
	PostSubmitRating(context.Context, *SubmitRatingDraft) (*Rating, error)
	mustEmbedUnimplementedDataStoreServer()
}

// UnimplementedDataStoreServer must be embedded to have forward compatible implementations.
type UnimplementedDataStoreServer struct {
}

func (UnimplementedDataStoreServer) GetUserClaims(context.Context, *UserRequest) (*UserClaimsResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method GetUserClaims not implemented")
}
func (UnimplementedDataStoreServer) GetSubmissions(context.Context, *SubmissionRequest) (*SubmissionsResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method GetSubmissions not implemented")
}
func (UnimplementedDataStoreServer) GetSubmissionDetails(context.Context, *DetailsSubmissionRequest) (*DetailsSubmissionResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method GetSubmissionDetails not implemented")
}
func (UnimplementedDataStoreServer) GetSubmissionRatings(context.Context, *RatingsSubmissionRequest) (*RatingsSubmissionResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method GetSubmissionRatings not implemented")
}
func (UnimplementedDataStoreServer) PostNewSubmissionRating(context.Context, *NewSubmissionRatingRequest) (*Rating, error) {
	return nil, status.Errorf(codes.Unimplemented, "method PostNewSubmissionRating not implemented")
}
func (UnimplementedDataStoreServer) PostPartialRating(context.Context, *PartialRatingRequest) (*PartialRating, error) {
	return nil, status.Errorf(codes.Unimplemented, "method PostPartialRating not implemented")
}
func (UnimplementedDataStoreServer) PostSubmitRating(context.Context, *SubmitRatingDraft) (*Rating, error) {
	return nil, status.Errorf(codes.Unimplemented, "method PostSubmitRating not implemented")
}
func (UnimplementedDataStoreServer) mustEmbedUnimplementedDataStoreServer() {}

// UnsafeDataStoreServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to DataStoreServer will
// result in compilation errors.
type UnsafeDataStoreServer interface {
	mustEmbedUnimplementedDataStoreServer()
}

func RegisterDataStoreServer(s grpc.ServiceRegistrar, srv DataStoreServer) {
	s.RegisterService(&DataStore_ServiceDesc, srv)
}

func _DataStore_GetUserClaims_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(UserRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DataStoreServer).GetUserClaims(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/data_store.DataStore/GetUserClaims",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DataStoreServer).GetUserClaims(ctx, req.(*UserRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _DataStore_GetSubmissions_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(SubmissionRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DataStoreServer).GetSubmissions(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/data_store.DataStore/GetSubmissions",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DataStoreServer).GetSubmissions(ctx, req.(*SubmissionRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _DataStore_GetSubmissionDetails_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(DetailsSubmissionRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DataStoreServer).GetSubmissionDetails(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/data_store.DataStore/GetSubmissionDetails",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DataStoreServer).GetSubmissionDetails(ctx, req.(*DetailsSubmissionRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _DataStore_GetSubmissionRatings_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(RatingsSubmissionRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DataStoreServer).GetSubmissionRatings(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/data_store.DataStore/GetSubmissionRatings",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DataStoreServer).GetSubmissionRatings(ctx, req.(*RatingsSubmissionRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _DataStore_PostNewSubmissionRating_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(NewSubmissionRatingRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DataStoreServer).PostNewSubmissionRating(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/data_store.DataStore/PostNewSubmissionRating",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DataStoreServer).PostNewSubmissionRating(ctx, req.(*NewSubmissionRatingRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _DataStore_PostPartialRating_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(PartialRatingRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DataStoreServer).PostPartialRating(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/data_store.DataStore/PostPartialRating",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DataStoreServer).PostPartialRating(ctx, req.(*PartialRatingRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _DataStore_PostSubmitRating_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(SubmitRatingDraft)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(DataStoreServer).PostSubmitRating(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/data_store.DataStore/PostSubmitRating",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(DataStoreServer).PostSubmitRating(ctx, req.(*SubmitRatingDraft))
	}
	return interceptor(ctx, in, info, handler)
}

// DataStore_ServiceDesc is the grpc.ServiceDesc for DataStore service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var DataStore_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "data_store.DataStore",
	HandlerType: (*DataStoreServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "GetUserClaims",
			Handler:    _DataStore_GetUserClaims_Handler,
		},
		{
			MethodName: "GetSubmissions",
			Handler:    _DataStore_GetSubmissions_Handler,
		},
		{
			MethodName: "GetSubmissionDetails",
			Handler:    _DataStore_GetSubmissionDetails_Handler,
		},
		{
			MethodName: "GetSubmissionRatings",
			Handler:    _DataStore_GetSubmissionRatings_Handler,
		},
		{
			MethodName: "PostNewSubmissionRating",
			Handler:    _DataStore_PostNewSubmissionRating_Handler,
		},
		{
			MethodName: "PostPartialRating",
			Handler:    _DataStore_PostPartialRating_Handler,
		},
		{
			MethodName: "PostSubmitRating",
			Handler:    _DataStore_PostSubmitRating_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "data_store.proto",
}

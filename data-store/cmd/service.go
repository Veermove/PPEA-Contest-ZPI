package main

import data_store "zpi/data-store/pb"

type DataStore struct {
	data_store.UnimplementedDataStoreServer
}

package main

import (
	"fmt"
	"os"
	"strconv"
	"sync"
	"sync/atomic"
	"time"
	"chainmaker.org/chainmaker/pb-go/v2/common"
	chainmaker_sdk_go "chainmaker.org/chainmaker/sdk-go/v2"
)

var MAX_Count = 100
var MAX_CONNECT = 3000
var wg = sync.WaitGroup{}
var params = []*common.KeyValuePair{{Key: "file_name", Value: []byte("test3")}, {Key: "file_hash", Value: []byte("test4")}}
var successCount uint64 = 0

//var latency []float64
//TODO:调整交易池大小 5000000
func init() {

}

func createClientWithConfig(path string) (*chainmaker_sdk_go.ChainClient, error) {
	chainClient, err := chainmaker_sdk_go.NewChainClient(chainmaker_sdk_go.WithConfPath(path))
	if err != nil {
		return nil, err
	}
	return chainClient, nil
}
func invokeUserContract(client *chainmaker_sdk_go.ChainClient, contractName, method, txId string, params []*common.KeyValuePair, withSyncResult bool) error {
	//now := time.Now().UnixNano()
	resp, err := client.InvokeContract(contractName, method, txId, params, -1, withSyncResult)
	if err != nil {
		return err
	}
	if resp.Code != common.TxStatusCode_SUCCESS {
		return fmt.Errorf("invoke contract failed, [code:%d]/[msg:%s]\n", resp.Code, resp.Message)
	}
	//latency = append(latency, float64(resp.TxTimestamp-now)/1e6)
	//if !withSyncResult {
	//	fmt.Printf("invoke contract success, resp: [code:%d]/[msg:%s]/[txId:%s]\n", resp.Code, resp.Message, resp.ContractResult.Result)
	//} else {
	//	fmt.Printf("invoke contract success, resp: [code:%d]/[msg:%s]/[contractResult:%s]\n", resp.Code, resp.Message, resp.ContractResult)
	//}
	return nil
}

//func queryUserContract(client *chainmaker_sdk_go.ChainClient, contractName, method, txId string, params []*common.KeyValuePair, withSyncResult bool) error {
//	//now := time.Now().UnixNano()
//	resp, err := client.InvokeContract(contractName, method, txId, params, -1, withSyncResult)
//	if err != nil {
//		return err
//	}
//	if resp.Code != common.TxStatusCode_SUCCESS {
//		return fmt.Errorf("invoke contract failed, [code:%d]/[msg:%s]\n", resp.Code, resp.Message)
//	}
//
//	return nil
//}
func invokeChainCode(client *chainmaker_sdk_go.ChainClient, num int) {
	var params = []*common.KeyValuePair{{Key: "file_name", Value: []byte("test" + strconv.Itoa(num))}, {Key: "file_hash", Value: []byte("test" + strconv.Itoa(num))}}
	for i := 0; i < MAX_Count; i++ {
		err := invokeUserContract(client, "kv", "save", "", params, true)
		if err == nil {
			atomic.AddUint64(&successCount, 1)
		}
	}
	wg.Done()
}
func queryChainCode(client *chainmaker_sdk_go.ChainClient, num int) {
	var params = []*common.KeyValuePair{{Key: "file_hash", Value: []byte("test" + strconv.Itoa(num))}}
	for i := 0; i < MAX_Count; i++ {
		err := invokeUserContract(client, "kv", "findByFileHash", "", params, true)
		if err == nil {
			atomic.AddUint64(&successCount, 1)
		}
	}
	wg.Done()
}
func main() {
	paths := []string{"./sdk_config.yml", "./sdk_config2.yml", "./sdk_config3.yml", "./sdk_config4.yml"}
	err := error(nil)
	clients := make([]*chainmaker_sdk_go.ChainClient, MAX_CONNECT)
	for i := 0; i < MAX_CONNECT; i++ {
		clients[i], err = createClientWithConfig(paths[i/(MAX_CONNECT/4)])
		if err != nil {
			os.Exit(1)
		}
	}
	fmt.Println("==========CREATED CHAIN CLIENTS==============")
	wg.Add(2 * MAX_CONNECT)
	for i := 0; i < MAX_CONNECT; i++ {
		go invokeChainCode(clients[i], i)
		go queryChainCode(clients[i], i)
	}
	timeStart := time.Now().UnixNano()
	wg.Wait()
	timeCount := MAX_Count * MAX_CONNECT
	count := float64(timeCount)
	timeEnd := time.Now().UnixNano()
	timeResult := float64((timeEnd-timeStart)/1e6) / 1000.0
	//min, _ := stats.Min(latency)
	//max, _ := stats.Max(latency)
	//mean, _ := stats.Mean(latency)
	fmt.Println("Throughput:", timeCount, "Duration:", strconv.FormatFloat(timeResult, 'g', 30, 32)+" s", "TPS:", count/timeResult, "success rate:", float64(successCount)/float64(timeCount)) //"\nmin latency:", min, "max latency:", max, "mean latency:", mean

}

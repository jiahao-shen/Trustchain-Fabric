# 链结构

链：

​	id：trustchain

​	name：trustchain

组织一：

​	id：public

​	name：public

​	node: 

​		-peer1：192.168.0.160 20201 30301 #20201为RPC端口 30301为P2P端口

​		-peer2：192.168.0.144 20201 30301

组织二：

​	id：buaa

​	name：buaa

​	node：

​		-buaaPeer3：192.168.0.160 20202 30302

​		-buaaPeer4：192.168.0.144 20202 30302

sdk_config主要用组织public进行交易的发布，buaa组织为维护用的隐藏组织，不实际参与链的管理。

检查链节点情况:

```
cat ./release/*/log/system.log |grep "ERROR\|put block\|all necessary"
```

```
ps -ef|grep chainmaker | grep -v grep
```

# CMC工具使用说明

## 智能合约相关

go合约必须打包成.7z格式，细节需要看https://docs.chainmaker.org.cn/v2.3.1/html/instructions/%E4%BD%BF%E7%94%A8Golang%E8%BF%9B%E8%A1%8C%E6%99%BA%E8%83%BD%E5%90%88%E7%BA%A6%E5%BC%80%E5%8F%91.html

下面所有命令都需要在/home/node5/Desktop/trustchain/tools/cmc下执行

### 部署GO合约

./cmc client contract user create \\

--contract-name=fact \\ 

--runtime-type=DOCKER_GO \\

--byte-code-path=./testdata/claim-docker-go-demo/docker-fact.7z \\

--version=1.0 \\

--sdk-conf-path=./testdata/sdk_config.yml \\

--admin-key-file-paths=./testdata/crypto-config/public/user/admin1/admin1.tls.key,./testdata/crypto-config/buaa/user/buaaAdmin/buaaAdmin.tls.key \\

--admin-crt-file-paths=./testdata/crypto-config/public/user/admin1/admin1.tls.crt,./testdata/crypto-config/buaa/user/buaaAdmin/buaaAdmin.tls.crt \\

--sync-result=true \\

--params="{}"

### 调用

./cmc client contract user invoke \\

--contract-name=docker_test \\

 --method=save \\

--sdk-conf-path=./testdata/sdk_config.yml \\

--params="{\"file_name\":\"name006\",\"file_hash\":\"ab3456df5799b87c77e7f88\",\"time\":\"6543234\"}"

### 查询

./cmc client contract user get \\

--contract-name=docker_test \\

--method=findByFileHash \\

--sdk-conf-path=./testdata/sdk_config.yml \

--params="{\"file_hash\":\"ab3456df5799b87c77e7f88\"}"

# 远程连接说明

需要将crypto-config文件夹和sdk_config.yml放置于资源目录下，并修改sdk_config密钥的相关路径，这两天我写个springboot的chainmakerConfig的demo。
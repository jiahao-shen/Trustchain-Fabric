# Trustchain-Fabric

## 版本信息
- hyperledger/fabric-tools: 2.5.0
- hyperledger/fabric-orderer: 2.5.0
- hyperledger/fabric-peer: 2.5.0
- hyperledger/ccenv: 2.5.0
- hyperledger/baseod: 2.5.0
- hyperledger/fabric-ca: 1.5.6

## 基本安装
1. 下载HyperLedger Fabric最新版本
```bash
curl -sSLO https://raw.githubusercontent.com/hyperledger/fabric/main/scripts/install-fabric.sh
chmod +x install-fabric.sh
./install-fabric.sh docker samples binary
```
2. 运行测试环境
```bash
cd fabric-samples/test-network
./network.sh up
```

## Fabric-CA
1. 运行Fabric-CA服务器
```bash
cd fabric-ca/server
docker-compose up -d
```

2. 登录Fabric-CA的admin账户
```bash
cd fabric-ca/client
export FABRIC_CA_CLIENT_HOME=$PWD

fabric-ca-client enroll -u https://admin:admin@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem -M ca.trustchain.com
```

3. 注册orderer节点和管理员账户
```bash
cd fabric-ca/client
export FABRIC_CA_CLIENT_HOME=$PWD

fabric-ca-client register -d --id.name orderer1.trustchain.com --id.secret Beihang2022 --id.type orderer -u https://admin:admin@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem
fabric-ca-client enroll -u https://orderer1.trustchain.com:Beihang2022@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem --csr.hosts orderer1.trustchain.com -M orderer1.trustchain.com

fabric-ca-client register -d --id.name orderer1admin --id.secret Beihang2022 --id.type client -u https://admin:admin@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem
fabric-ca-client enroll -u https://orderer1admin:Beihang2022@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem --csr.hosts orderer1.trustchain.com -M orderer1admin
```

4. 注册public节点和管理员账户
```bash
cd fabric-ca/client
export FABRIC_CA_CLIENT_HOME=$PWD

# peer1
fabric-ca-client register -d --id.name peer1.public.trustchain.com --id.secret Beihang2022 --id.type peer -u https://admin:admin@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem
fabric-ca-client enroll -u https://peer1.public.trustchain.com:Beihang2022@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem --csr.hosts peer1.public.trustchain.com -M peer1.public.trustchain.com

# peer1管理员
fabric-ca-client register -d --id.name peer1publicadmin --id.secret Beihang2022 --id.type client -u https://admin:admin@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem
fabric-ca-client enroll -u https://peer1publicadmin:Beihang2022@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem --csr.hosts peer1.public.trustchain.com -M peer1publicadmin

# peer2
fabric-ca-client register -d --id.name peer2.public.trustchain.com --id.secret Beihang2022 --id.type peer -u https://admin:admin@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem
fabric-ca-client enroll -u https://peer2.public.trustchain.com:Beihang2022@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem --csr.hosts peer2.public.trustchain.com -M peer2.public.trustchain.com

# peer2管理员
fabric-ca-client register -d --id.name peer2publicadmin --id.secret Beihang2022 --id.type client -u https://admin:admin@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem
fabric-ca-client enroll -u https://peer2publicadmin:Beihang2022@localhost:7054 --tls.certfiles ../server/data/tls-cert.pem --csr.hosts peer2.public.trustchain.com -M peer2publicadmin

```







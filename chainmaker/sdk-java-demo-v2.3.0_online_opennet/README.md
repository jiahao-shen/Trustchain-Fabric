# 后端链接说明

JAVA版本目前测试下来只有JDK1.8_202下能够正常访问，下载链接见[Index of java-local/jdk/8u202-b08 (huaweicloud.com)](https://repo.huaweicloud.com/java/jdk/8u202-b08/)

# MAVEN

需要将lib中的netty-tcnative-openssl包手动加载到项目中

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.5</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>chainmaker.sdk</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo</name>
    <description>java sdk Demo</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.chainmaker</groupId>
            <artifactId>chainmaker-sdk-java</artifactId>
            <version>2.3.0</version>
        </dependency>
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-tcnative-openssl-static</artifactId>
            <scope>system</scope>
            <version>2.0.39.Final</version>
            <systemPath>${project.basedir}/lib/netty-tcnative-openssl-static-2.0.39.Final.jar</systemPath>
        </dependency>

        <!-- java-sdk中的依赖 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.11.1</version>
        </dependency>
        <dependency>
            <groupId>commons-collections</groupId>
            <artifactId>commons-collections</artifactId>
            <version>3.2.2</version>
        </dependency>

        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcpkix-jdk15on</artifactId>
            <version>1.62</version>
        </dependency>

        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.62</version>
        </dependency>

        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.2</version>
        </dependency>

        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java-util</artifactId>
            <version>3.2.0</version>
            <exclusions>
                <exclusion>
                    <groupId>com.google.guava</groupId>
                    <artifactId>guava</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.google.protobuf</groupId>
                    <artifactId>protobuf-java</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-handler</artifactId>
            <version>4.1.53.Final</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-api</artifactId>
            <version>1.23.0</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty</artifactId>
            <version>1.23.0</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>1.23.0</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>1.23.0</version>
        </dependency>
<!--        <dependency>-->
<!--            <groupId>org.projectlombok</groupId>-->
<!--            <artifactId>lombok</artifactId>-->
<!--            <version>1.18.16</version>-->
<!--        </dependency>-->

        <dependency>
            <groupId>org.web3j</groupId>
            <artifactId>abi</artifactId>
            <version>5.0.0</version>
        </dependency>

<!--        <dependency>-->
<!--            <groupId>mysql</groupId>-->
<!--            <artifactId>mysql-connector-java</artifactId>-->
<!--            <version>8.0.21</version>-->
<!--        </dependency>-->
        <dependency>
            <groupId>net.java.dev.jna</groupId>
            <artifactId>jna</artifactId>
            <version>5.6.0</version>
        </dependency>
        <!--spring boot-->
        <!--        <dependency>-->
        <!--            <groupId>ch.qos.logback</groupId>-->
        <!--            <artifactId>logback-classic</artifactId>-->
        <!--            <version>1.2.3</version>-->
        <!--        </dependency>-->

        <!--spring boot-->
        <!--        <dependency>-->
        <!--            <groupId>org.yaml</groupId>-->
        <!--            <artifactId>snakeyaml</artifactId>-->
        <!--            <version>1.29</version>-->
        <!--        </dependency>-->


    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <!-- 打包时包含引入的外部jar包 -->
                    <includeSystemScope>true</includeSystemScope>
                </configuration>
                <version>2.2.2.RELEASE</version>
            </plugin>
        </plugins>
    </build>

</project>

```

# 新增节点需要做的工作

## 1. 证书

需要手动生成证书路径，目录结构见crypto-config，命名结构还需商议。

## 2. 配置文件

需要手动生成sdk_config.yml，并在里面修改证书的相关路径，命名结构还需商议。

## 3. 日志清理

服务器新加入节点后需要定时清理节点产生的日志文件，清理脚本位于/home/node4/Desktop/trustchain/clean.sh（以node5服务器为例）

```shell
find /home/node4/Desktop/trustchain/release/buaa-peer3/log/ -mtime +3 -exec rm -rf {} \;
```

定时任务通过下述命令进行修改

```shell
crontab -e
```


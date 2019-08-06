package com.xiaojun.vpn;

/**
 * Crated by xiaojun on 2019/8/1 11:06
 */
public class JsonConfig {
    public static String localJson = "{\n" +
            "  \"inbounds\": [\n" +
            "    {\n" +
            "      \"port\": 16823\n" +
            "      \"protocol\": \"socks\", \n" +
            "\"listen\":\"127.0.0.1\",\n" +
            "      \"sniffing\": {\n" +
            "        \"enabled\": true,\n" +
            "        \"destOverride\": [\"http\", \"tls\"]\n" +
            "      },\n" +
            "      \"settings\": {\n" +
            "        \"auth\": \"noauth\"  \n" +
            "\"udp\":true\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"outbounds\": [\n" +
            "    {\n" +
            "      \"protocol\": \"vmess\", \n" +
            "      \"settings\": {\n" +
            "        \"vnext\": [\n" +
            "          {\n" +
            "            \"address\": \"127.0.0.1\", \n" +
            "            \"port\": 3000,  \n" +
            "            \"users\": [\n" +
            "              {\n" +
            "                \"id\": \"b831381d-6324-4d53-ad4f-8cda48b30811\",  \n" +
            "                \"alterId\": 64 \n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    public static String huangsuoGoogle = "{\n" +
            "  \"port\": 10808,\n" +
            "  \"log\": {\n" +
            "    \"access\": \"\",\n" +
            "    \"error\": \"\",\n" +
            "    \"loglevel\": \"info\"\n" +
            "  },\n" +
            "  \"inbound\": {\n" +
            "    \"protocol\": \"socks\",\n" +
            "    \"listen\": \"127.0.0.1\",\n" +
            "    \"settings\": {\n" +
            "      \"auth\": \"noauth\",\n" +
            "      \"udp\": true\n" +
            "    }\n" +
            "  },\n" +
            "  \"inboundDetour\": [\n" +
            "    {\n" +
            "      \"protocol\": \"http\",\n" +
            "      \"port\": 10845,\n" +
            "      \"settings\": {},\n" +
            "      \"listen\": \"127.0.0.1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"protocol\": \"dokodemo-door\",\n" +
            "      \"listen\": \"127.0.0.1\",\n" +
            "      \"port\": 10846,\n" +
            "      \"settings\": {\n" +
            "        \"network\": \"tcp\",\n" +
            "        \"timeout\": 0,\n" +
            "        \"followRedirect\": true\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"protocol\": \"dokodemo-door\",\n" +
            "  \"listen\": \"127.0.0.1\",\n" +
            "      \"port\": 5353,\n" +
            "      \"settings\": {\n" +
            "        \"network\": \"tcp\",\n" +
            "        \"timeout\": 120,\n" +
            "        \"address\": \"208.67.222.222\",\n" +
            "        \"port\": 53,\n" +
            "        \"followRedirect\": false\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"outbound\": {\n" +
            "    \"protocol\": \"vmess\",\n" +
            "    \"settings\": {\n" +
            "      \"vnext\": [\n" +
            "        {\n" +
            "          \"address\": \"127.0.0.1\",\n" +
            "          \"port\": 3000,\n" +
            "          \"users\": [\n" +
            "            {\n" +
            "              \"id\": \"3061e463-a13e-4b51-81ec-86d84aab451d\",\n" +
            "              \"alterId\": 32\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"streamSettings\": {\n" +
            "      \"network\": \"tcp\",\n" +
            "      \"security\": \"none\",\n" +
            "      \"tlsSettings\": {\n" +
            "        \"allowInsecure\": true\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"transport\": {},\n" +
            "  \"#lib2ray\": {\n" +
            "    \"enabled\": true,\n" +
            "    \"listener\": {\n" +
            "      \"onUp\": \"#none\",\n" +
            "      \"onDown\": \"#none\"\n" +
            "    },\n" +
            "    \"env\": [\n" +
            "      \"V2RayDNSPort=5353\",\n" +
            "      \"DNSForwardingProxyPort=5350\",\n" +
            "      \"V2RaySocksPort=10808\"\n" +
            "    ],\n" +
            "    \"render\": [],\n" +
            "    \"escort\": [],\n" +
            "    \"vpnservice\": {\n" +
            "      \"Target\": \"${datadir}tun2socks\",\n" +
            "      \"Args\": [\n" +
            "        \"--netif-ipaddr\",\n" +
            "        \"26.26.26.2\",\n" +
            "        \"--netif-netmask\",\n" +
            "        \"255.255.255.0\",\n" +
            "        \"--socks-server-addr\",\n" +
            "        \"127.0.0.1:$V2RaySocksPort\",\n" +
            "        \"--tunfd\",\n" +
            "        \"3\",\n" +
            "        \"--tunmtu\",\n" +
            "        \"1500\",\n" +
            "        \"--sock-path\",\n" +
            "        \"/dev/null\",\n" +
            "        \"--loglevel\",\n" +
            "        \"4\",\n" +
            "        \"--enable-udprelay\"\n" +
            "      ],\n" +
            "      \"VPNSetupArg\": \"m,1500 a,26.26.26.1,24 r,0.0.0.0,0 d,208.67.222.222\"\n" +
            "    }\n" +
            "  }\n" +
            "}";
}

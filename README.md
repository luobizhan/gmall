# gmall 本地修改版本 1.0 

gmall_user_service端口号：8070
gmall_user_web端口号: 8071

gmall_manage_service端口号：8081
gmall_manage_web端口号: 8082

gmall_item_web端口号：8060

gmall_search_web端口号：8090
gmall_search_service端口号：8091

gmall_cart_web端口号：8100
gmall_cart_service端口号：8101

gmall_passport_web端口号：8110
gmall_user_service端口号：8070

gmall_order_web端口号：8200
gmall_order_service端口号：8201

gmall_payment_web端口号：8210
gmall_payment_service端口号：8211

#es表结构
PUT gmall
{
  "mappings": {
    "PmsSkuInfo":{
      "properties": {
        "id":{
          "type": "keyword",
          "index": true
        },
        "skuName":{
          "type": "text",
          "analyzer": "ik_max_word"
        },
        "skuDesc":{
          "type": "text",
          "analyzer": "ik_smart"
        },
        "catalog3Id":{
          "type": "keyword"
        },
        "price":{
          "type": "double"
        },
        "skuDefaultImg":{
          "type": "keyword"
        },
        "hostScore":{
          "type": "keyword"
        },
        "productId":{
          "type": "keyword"
        },
        "skuAttrValueList":{
          "properties": {
            "attrId":{
              "type": "keyword"
            },
            "valueId":{
              "type": "keyword"
            }
          }
        }
      }  
    }
  }
}
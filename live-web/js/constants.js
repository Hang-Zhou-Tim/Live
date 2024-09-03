let server_addr="http://110.40.129.233:8080";

let apiUrl = "/live/api";
let sendSmsUrl = server_addr + apiUrl + "/userLogin/sendLoginCode";
let loginUrl = server_addr + apiUrl + "/userLogin/login";
let homePageUrl = server_addr + apiUrl + "/home/initPage";
let startLiving = server_addr + apiUrl + "/room/startLiveStreamRoom";
let closeLiving = server_addr + apiUrl + "/room/closeLiveStreamRoom";
let anchorConfigUrl = server_addr + apiUrl + "/room/getAnchorConfig";
let listLivingRoomUrl = server_addr + apiUrl +"/room/list";
let getImConfigUrl = server_addr + apiUrl + "/im/getImConfig";
let listGiftConfigUrl = server_addr + apiUrl + "/gift/listGift";
let sendGiftUrl = server_addr + apiUrl + "/gift/send";
let getAllCurrencyAmountsUrl = server_addr + apiUrl + "/payment/getAllCurrencyAmounts";
let buyCurrencyUrl = server_addr + apiUrl + "/payment/buyCurrency";
let onlinePkUrl = server_addr + apiUrl + "/room/joinOnlinePK";
let prepareRedPacketUrl = server_addr + apiUrl + "/gift/prepareRedPacket";
let startRedPacketUrl = server_addr + apiUrl + "/gift/startRedPacket";
let getRedPacketUrl = server_addr + apiUrl + "/gift/snatchRedPacket";
let queryOnlinePkUserUrl = server_addr + apiUrl + "/room/queryOnlinePkUserId";
let queryShopInfoUrl = server_addr + apiUrl + "/shop/listSkuInfo";
let queryShopDetailInfoUrl = server_addr + apiUrl + "/shop/querySkuDetailById";
let addShopCartUrl = server_addr + apiUrl + "/shop/addItemToCart";
let getCartInfoUrl = server_addr + apiUrl + "/shop/getCartInfo";
let removeFromCartUrl = server_addr + apiUrl + "/shop/removeItemFromCart"
let createPreOrderInfoUrl = server_addr + apiUrl + "/shop/prepareOrder";
let payNowUrl = server_addr + apiUrl + "/shop/payNow";
queryOnlinePkUserId
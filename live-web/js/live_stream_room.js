new Vue({
    el: '#app',
    data: {
        form: {
            review: ""
        },
        chatList: [],
        giftList: [],
        canvas: {},
        player: {},
        parser: {},
        websock: null,
        roomId: -1,
        anchorId: -1,
        isLogin: false,
        wsServer: '',
        initInfo: {},
        imServerConfig: {},
        showGiftRank: false,
        rankList: [],
        accountInfo: {},
        showBankInfo: false,
        lastPayBtnId: -1,
        currencyItemList: [],
        nickname:'',
        currentBalance:0,
        qrCode: 'true',
        dlProgress: 10,
        redPacketConfigCode: '',
        showPrepareBtn:false,
        showStartBtn: false,
        showShopTab: false,
        showCarTab:false,
        closeLivingRoomDialog: false,
        livingRoomHasCloseDialog: false,
        timer: null,
        startingRedPacket:false,
        shopInfoList:[],
        shopDetailInfo:{},
        shopCartInfo:[],
        showOrderTab: false,
        address:'',
        shopCartTotalPrice:0,
    },

    mounted() {
        this.roomId = getQueryStr("roomId");    //Get Room Id from url of the webpage.
        this.anchorConfig();
        this.initSvga();
        this.initGiftConfig();
        this.listAllCurrencyAmounts();
        
    },

    beforeDestroy() {
        this.timer = null;
    },

    methods: {

        turnBackShopCar:function() {
                this.showOrderTab=false;
        },


        prepareOrder: function() {
            if(this.address=='') {
                this.$message.error('Please Write Receiver Address!');
                return;
            }
            console.log(this.shopCartInfo);
            if(this.shopCartInfo.length<1) {
                this.$message.error('Please Add Goods Before Payment!');
                return;
            }
            
            this.$message.success('Order Generated.');
            this.createPreOrderInfo();
        },

        payNow:function() {
            let data = new FormData();
            data.append("roomId",getQueryStr("roomId"));
            var that = this;
            httpPost(payNowUrl,data).then(
                resp=>{
                    if(isSuccess(resp) && resp.data) {
                        that.$message.success("Payment Succeeds");
                        that.hiddenGreyTab();
                        that.listPayCurrency();
                    }
                }
            )    
        },
        createPreOrderInfo:function() {
            let data = new FormData();
            data.append("roomId",getQueryStr("roomId"));
            var that = this;
            httpPost(createPreOrderInfoUrl,data).then(
                resp=>{
                    if (isSuccess(resp)) {
                        that.showOrderTab = true;
                        console.log(resp);
                    }
                }
            )
        },

        queryShopInfo: function() {
            let data = new FormData();
            data.append("anchorId",this.initInfo.anchorId);
            var that = this;
            httpPost(queryShopInfoUrl,data).then(
                resp => {
                    console.log(resp.data);
                    if(isSuccess(resp)) {
                        that.shopInfoList = resp.data;
                    }
                }
            )
        },

        removeShopCartItem: function(skuId) {
            let data = new FormData();
            data.append("roomId",getQueryStr("roomId"));
            data.append("skuId",skuId);
            var that = this;
            httpPost(removeFromCartUrl,data).then(
                resp=>{
                    if(isSuccess(resp)) {
                      that.$message.success('The Goods Has Been Removed!');
                      that.getCartInfo(false);
                    }
                }
            )
        },

        queryShopDetailInfo:function(skuId) {
            let data = new FormData();
            data.append("skuId",skuId);
            var that = this;
            httpPost(queryShopDetailInfoUrl,data).then(
                resp=>{
                    if(isSuccess(resp)) {
                        that.shopDetailInfo = resp.data;
                    }
                }
            )
        },

        addShopCart:function(skuId) {
            let data = new FormData();
            data.append("skuId",skuId);
            data.append("roomId",getQueryStr("roomId"));
            var that = this;
            httpPost(addShopCartUrl,data).then(
                resp=>{
                    if (isSuccess(resp)) {
                        window.alert('This item has been added to the cart!');
                        that.$message.success('Added to the Cart');
                        that.hiddenCarTab();
                    }
                }
            )
        },

        initGiftConfig:function() {
            let that = this;
            httpPost(listGiftConfigUrl, {})
            .then(resp => {
                if (isSuccess(resp)) {
                    console.log(resp.data);
                    that.giftList = resp.data;
                }
            });
        },

        getCartInfo:function(showOrderStatus) {
            let data = new FormData();
            data.append("roomId",getQueryStr("roomId"));
            var that = this;
            httpPost(getCartInfoUrl,data).then(
                resp=>{
                    if(isSuccess(resp)) {
                        that.shopCartInfo=resp.data.shopCartItemRespDTOS;
                        that.shopCartTotalPrice = resp.data.totalPrice;
                        that.showOrderTab = showOrderStatus;
                        console.log(this.shopCartTotalPrice);
                    }
                }
            )
        },

        toShowShopTab: function(skuId) {
            this.showShopTab = true;
            this.queryShopDetailInfo(skuId);
        },

        toShowCarTab: function() {
            this.showCarTab = true;
            this.getCartInfo(false);    
        },

        hiddenCarTab: function(){
            this.showCarTab = false;
        },

        hiddenGreyTab: function() {
            this.showShopTab = false;
            this.showCarTab = false
        },

        initSvga: function () {
            canvas = document.getElementById('svga-wrap');
            player = new window.SVGA.Player(canvas);
            parser = new window.SVGA.Parser(canvas);
         
        },

        listAllCurrencyAmounts: function() {
            let data = new FormData();
            data.append("type",0);
            let that = this;
            httpPost(getAllCurrencyAmountsUrl, data)
                .then(resp => {
                console.log(resp);
                if (isSuccess(resp)) {
                    that.currencyItemList = resp.data.currencyItemList;
                    that.currentBalance = resp.data.currentBalance;
                }
            });  
        },

        sendGift: function(giftId) {
            let data = new FormData();
			data.append("giftId",giftId);
            data.append("type",0);
            data.append("roomId",getQueryStr("roomId"));
            data.append("receiverId",this.initInfo.anchorId);
            let that = this;
            httpPost(sendGiftUrl, data)
            .then(resp => {
                if (!isSuccess(resp)) {
                    that.$message.error(resp.msg);
                }
            });  
        },

        //Render Gift Animation with SVGA
        playGiftSvga: function (url) {
            player.clearsAfterStop = true;
            player.stopAnimation();
            console.log(url);
            parser.load(url, function (videoItem) {
                player.loops = 1; // Set loop time to 1
                player.setVideoItem(videoItem);
                player.startAnimation();
                player.onFinished(function () {
                    console.log("Animation Stops!!!!");
                });
            });
        },
        //Choose Cooresponding Currency to pay.
        buyCurrency: function(currencyId) {
            let data = new FormData();
			data.append("currencyId",currencyId);
            data.append("payChannel",1);
            data.append("paySource",1);
            let that = this;
            httpPost(buyCurrencyUrl, data)
            .then(resp => {
                if (!isSuccess(resp)) {
                    that.$message.error('Topup Failed!');
                } else {
                    that.$message.success('Topup Succeeds!');
                    that.listAllCurrencyAmounts();
                }
            });  
        },

        showBankInfoTab:function() {
          this.showBankInfo=true;
        },

        hiddenBankInfoTabNow:function() {
            this.showBankInfo = false;
        },
        
        //Initialise roomVO for Room Id
        anchorConfig: function () {
            let data = new FormData();
			data.append("roomId",getQueryStr("roomId"));
            var that = this;
            httpPost(anchorConfigUrl, data)
                .then(resp => {
                    if (isSuccess(resp)) {
                        if(resp.data.roomId>0) {
                            that.initInfo = resp.data;
                            that.connectImServer();
                            that.redPacketConfigCode = resp.data.redPacketConfigCode;
                            that.showPrepareBtn = (that.redPacketConfigCode != null);
                            that.queryShopInfo();
                        } else {
                            this.$message.error('Live Stream Room is Not Existed!');
                        }
                    }
                });
        },

        prepareRedPacket: function() {
            let data = new FormData();
			data.append("roomId",getQueryStr("roomId"));
            httpPost(prepareRedPacketUrl, data)
                .then(resp => {
                    if (isSuccess(resp)) {
                       if(!resp.data) {
                         this.$message.error(resp.msg);
                       } else {
                         this.showStartBtn = true;
                         this.showPrepareBtn = false;
                         this.$message.success('Red Packet Initisation Finished!');
                       }
                    }
                });
        },

        startSendRedPacket: function() {
                let data = new FormData();
                data.append("redPacketConfigCode",this.redPacketConfigCode);
                httpPost(startRedPacketUrl, data)
                    .then(resp => {
                        if (isSuccess(resp)) {
                            if(!resp.data) {
                                this.$message.error('Red Envelop Failed to Broadcast!');
                            } else {
                                this.showStartBtn = false;
                                this.$message.success('Broadcast Succeeds!');
                            }
                        }
                    });
        },
        

        connectImServer: function() {
            let that = this;
            httpPost(getImConfigUrl, {})
            .then(resp => {
                if (isSuccess(resp)) {
                    that.imServerConfig = resp.data;
                    let url = "ws://"+that.imServerConfig.wsImServerAddress+"/" + that.imServerConfig.token+"/"+that.initInfo.userId+"/1001/"+this.roomId;
                    console.log(url);
                    that.websock = new WebSocket(url);
                    that.websock.onmessage = that.websocketOnMessage;
                    that.websock.onopen = that.websocketOnOpen;
                    that.websock.onerror = that.websocketOnError;
                    that.websock.onclose = that.websocketClose;
                    console.log('Initialised WS Server!');
                }
            });
           
        },


        websocketOnOpen: function() {
            console.log('Conneciton Established!');
        },

        websocketOnError: function() {
            console.error('Errors!');
        },

        websocketOnMessage: function(e) { //Receive Data from WS Server
            let wsData = JSON.parse(e.data);
            if(wsData.code == 1001) {
                this.startHeartBeatJob();
            } else if (wsData.code == 1003) {
                let respData = JSON.parse(utf8ByteToUnicodeStr(wsData.body));
                //receiving instant message in live stream room
                if(respData.bizCode==5555) {
                    let respMsg = JSON.parse(respData.data);
                    let sendMsg = {"content": respMsg.content, "senderName": respMsg.senderName, "senderImg": respMsg.senderAvtar};
                    let msgWrapper = {"msgType": 1, "msg": sendMsg};
                    console.log(sendMsg);
                    this.chatList.push(msgWrapper);
                    //Scroll down
                    this.$nextTick(() => {
                        var div = document.getElementById('talk-content-box')
                        div.scrollTop = div.scrollHeight
                    })
                    //Send ACK confirm data
                } else if(respData.bizCode == 5556) {
                    //Send Gifts Succeeds!
                    let respMsg = JSON.parse(respData.data);
                    this.playGiftSvga(respMsg.url);
                } else if(respData.bizCode == 5557){
                    //Send Gifts Failed!
                    let respMsg = JSON.parse(respData.data);
                    this.$message.error(respMsg.msg);
                } else if (respData.bizCode == 5560) {
                    if(!this.startingRedPacket) {
                        this.startingRedPacket=true;
                        //Start Red Packets Rain.
                        let respMsg = JSON.parse(respData.data);
                        let redPacketConfig = JSON.parse(respMsg.redPacketConfig);
                        console.log(redPacketConfig.totalCount);
                        console.log(redPacketConfig.configCode);
                        initRedPacket(redPacketConfig.totalCount,redPacketConfig.configCode);
                    }
                   
                }
                this.sendAckCode(respData);
            }
        },

        sendAckCode: function(respData) {
            let jsonStr = {"userId": this.initInfo.userId, "appId": 10001,"msgId":respData.msgId};
            let bodyStr = JSON.stringify(jsonStr);
            let ackMsgStr = {"magic": 19231, "code": 1005, "len": bodyStr.length, "body": bodyStr};
            this.websocketSend(JSON.stringify(ackMsgStr));
        },
 
        websocketSend:function (data) {//Data Sent
            this.websock.send(data);
        },

        websocketClose: function (e) {  //Close
            window.alert("The connection is ended for being idle! Please refresh this page!");
            console.log('Close connection', e);
        },

        startHeartBeatJob: function() {
            console.log('Login Succeed!');
            let that = this;
            //Send heartbeats packets.
            let jsonStr = {"userId": this.initInfo.userId, "appId": 10001};
            let bodyStr = JSON.stringify(jsonStr);
            let heartBeatJsonStr = {"magic": 19231, "code": 1004, "len": bodyStr.length, "body": bodyStr};
            setInterval(function () {
                that.websocketSend(JSON.stringify(heartBeatJsonStr));
            }, 3000);
        },

        closeLivingRoom: function() {
            let data = new FormData();
			data.append("roomId",getQueryStr("roomId"));
            httpPost(closeLiving, data)
            .then(resp => {
                if (isSuccess(resp)) {
                    window.location.href='./live_stream_room_list.html';
                }
            });
        },


        sendReview: function () {
            if (this.form.review == '') {
                this.$message({
                    message: "Comment should not be empty!",
                    type: 'warning'
                });
                return;
            }
            let sendMsg = {"content": this.form.review, "senderName": this.initInfo.nickName, "senderImg": this.initInfo.avatar};
            let msgWrapper = {"msgType": 1, "msg": sendMsg};
            this.chatList.push(msgWrapper);
            //Send Message to IM server
            let msgBody = {"roomId":this.roomId,"type":1,"content":this.form.review,  "senderName": this.initInfo.nickName, "senderAvtar": this.initInfo.avatar};
            console.log(this.initInfo);
            let jsonStr = {"userId": this.initInfo.userId, "appId": 10001,"bizCode":5555,"data":JSON.stringify(msgBody)};
            let bodyStr = JSON.stringify(jsonStr);
            console.log('Send Message');
            let reviewMsg = {"magic": 19231, "code": 1003, "len": bodyStr.length, "body": bodyStr};
            console.log(JSON.stringify(reviewMsg));
            this.websocketSend(JSON.stringify(reviewMsg));
            this.form.review = '';
            this.$nextTick(() => {
                var div = document.getElementById('talk-content-box')
                div.scrollTop = div.scrollHeight
            })
        
        }

     
    }

});
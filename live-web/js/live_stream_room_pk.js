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
        nickname: '',
        currentBalance: 0,
        qrCode: 'true',
        dlProgress: 10,
        closeLivingRoomDialog: false,
        livingRoomHasCloseDialog: false,
        timer: null,
        pkUserId: 0,
        pkObjId: 0,
        lastChooseAnchorTab: '',
        lastChooseAnchorId: 0,
        anchorImg: '',
        pkObjImg: '',
        pkEnd: false
    },

    mounted() {
        this.roomId = getQueryStr("roomId");
        this.anchorConfig();
        this.initSvga();
        this.initGiftConfig();
        this.listAllCurrencyAmounts();
    },

    beforeDestroy() {
        this.timer = null;
    },

    methods: {

        initGiftConfig: function () {
            let that = this;
            httpPost(listGiftConfigUrl, {})
                .then(resp => {
                    if (isSuccess(resp)) {
                        that.giftList = resp.data;
                        console.log(resp.data);
                    }
                });
        },

        initSvga: function () {
            canvas = document.getElementById('svga-wrap');
            player = new window.SVGA.Player(canvas);
            parser = new window.SVGA.Parser(canvas);

        },

        listAllCurrencyAmounts: function () {
            let data = new FormData();
            data.append("type", 0);
            let that = this;
            httpPost(getAllCurrencyAmountsUrl, data)
                .then(resp => {
                    if (isSuccess(resp)) {
                        console.log(resp.data);
                        that.currencyItemList = resp.data.currencyItemList;
                        that.currentBalance = resp.data.currentBalance;
                    }
                });
        },

        sendGift: function (giftId) {
            if (this.pkEnd) {
                this.$message.success('pk already ends!');
                return;
            }
            let data = new FormData();
            let anchorId = this.lastChooseAnchorId;
            let userId = this.initInfo.userId;
            if (this.pkObjId == 0 || this.pkObjId == null) {
                this.$message.error('Anchors in the pk is not online!');
                return;
            }
            if (this.lastChooseAnchorId < 1000) {
                this.$message.error('Please select anchors to send gifts!');
                return;
            }
            if (anchorId == userId) {
                this.$message.error('Cannot send gift to yourself!');
                return;
            }
            data.append("giftId", giftId);
            data.append("type", 1);
            data.append("roomId", getQueryStr("roomId"));
            data.append("receiverId", anchorId);
            data.append("senderUserId", userId);
            let that = this;
            httpPost(sendGiftUrl, data)
                .then(resp => {
                    if (!isSuccess(resp)) {
                        that.$message.error('Sending Gift Fails! ');
                    }
                });
        },

        playGiftSvga: function (url) {
            player.clearsAfterStop = true;
            player.stopAnimation();
            //console.log(url);
            parser.load(url, function (videoItem) {
                player.loops = 1; // only loop for 1 time
                player.setVideoItem(videoItem);
                player.startAnimation();
                player.onFinished(function () {
                    console.log("Animation ends！！！");
                });
            });
        },
        //Select corresponding currency，and topup
        buyCurrency: function (currencyId) {
            let data = new FormData();
            data.append("currencyId", currencyId);
            data.append("payChannel", 1);
            data.append("paySource", 1);
            let that = this;
            httpPost(buyCurrencyUrl, data)
                .then(resp => {
                    if (!isSuccess(resp)) {
                        that.$message.error('Topup Failed');
                    } else {
                        that.$message.success('Topup Success');
                        that.listAllCurrencyAmounts();
                    }
                });
        },

        connectLiving: function () {
            let that = this;
            let data = new FormData();
            data.append("roomId", getQueryStr("roomId"));
            httpPost(onlinePkUrl, data)
                .then(resp => {
                    if (isSuccess(resp)) {
                        window.alert("Connected!");
                        that.$message.success('Connection succeeds!');
                    } else {
                        window.alert(resp.msg);
                        that.$message.success(resp.msg);
                    }
                });
        },

        showBankInfoTab: function () {
            this.showBankInfo = true;
        },

        hiddenBankInfoTabNow: function () {
            this.showBankInfo = false;
        },

        choosePkUserAnchor: function () {
            this.chooseAnchor('anchorVideo', this.pkUserId);
        },

        choosePkObjectAnchor: function () {
            if (this.pkObjId == null) {
                let data = new FormData();
                data.append("roomId", getQueryStr("roomId"));
                httpPost(queryOnlinePkUserUrl, data)
                .then(resp => {
                    if (isSuccess(resp)) {
                        if (resp.data != null) {
                            this.pkObjId = resp.data;
                        }
                        console.log(resp.data);
                    } else {
                        this.$message.error(resp.msg);
                    }
                });
            }
            
            this.chooseAnchor('subAnchorVideo', this.pkObjId);
        },

        //Choose Anchor to send gifts to.
        chooseAnchor: function (id, anchorId) {
            console.log('Select Anchor' + anchorId);
            this.lastChooseAnchorId = anchorId;
            let lastChooseAnchor = document.getElementById(this.lastChooseAnchorTab);
            if (lastChooseAnchor != undefined) {
                lastChooseAnchor.style.border = "rgba(255,165,0,0) 3px solid";
            }
            let currentChooseAnchor = document.getElementById(id);
            currentChooseAnchor.style.border = "rgba(255,165,0,1) 3px solid";
            this.lastChooseAnchorTab = id;
        },

        //Invoked when Initialise Live Broadcasting Room
        anchorConfig: function () {
            let data = new FormData();
            data.append("roomId", getQueryStr("roomId"));
            var that = this;
            httpPost(anchorConfigUrl, data)
                .then(resp => {
                    if (isSuccess(resp)) {
                        if (resp.data.roomId > 0) {
                            that.initInfo = resp.data;
                            that.anchorImg = that.initInfo.avatar;
                            that.pkUserId = that.initInfo.anchorId;
                            that.pkObjId = that.initInfo.pkObjId;
                            that.connectImServer();
                        } else {
                            this.$message.error('Room is not existed! ');
                        }
                    } else {
                        this.$message.error(resp.msg);
                    }
                });
        },


        connectImServer: function () {
            let that = this;
            httpPost(getImConfigUrl, {})
                .then(resp => {
                    if (isSuccess(resp)) {
                        that.imServerConfig = resp.data;
                        let url = "ws://" + that.imServerConfig.wsImServerAddress + "/" + that.imServerConfig.token + "/" + that.initInfo.userId + "/1001/" + this.roomId;
                        console.log(url);
                        that.websock = new WebSocket(url);
                        that.websock.onmessage = that.websocketOnMessage;
                        that.websock.onopen = that.websocketOnOpen;
                        that.websock.onerror = that.websocketOnError;
                        that.websock.onclose = that.websocketClose;
                        console.log('Initialise WS WebServer! ');
                    }
                });

        },


        websocketOnOpen: function () {
            console.log('Connection Established');
        },

        websocketOnError: function () {
            console.error('Conneciton Errors');
        },

        websocketOnMessage: function (e) { //Data Received
            let wsData = JSON.parse(e.data);
            //console.log(wsData);
            if (wsData.code == 1001) {
                this.startHeartBeatJob();
            } else if (wsData.code == 1003) {
                let respData = JSON.parse(utf8ByteToUnicodeStr(wsData.body));
                if (respData.bizCode == 5555) {
                    let respMsg = JSON.parse(respData.data);
                    let sendMsg = { "content": respMsg.content, "senderName": respMsg.senderName, "senderImg": respMsg.senderAvtar };
                    let msgWrapper = { "msgType": 1, "msg": sendMsg };
                    console.log(sendMsg);
                    this.chatList.push(msgWrapper);
                    //scroll down
                    this.$nextTick(() => {
                        var div = document.getElementById('talk-content-box')
                        div.scrollTop = div.scrollHeight
                    })
                } else if (respData.bizCode == 5557) {
                    //failed to send gifts
                    let respMsg = JSON.parse(respData.data);
                    this.$message.error(respMsg.msg);
                } else if (respData.bizCode == 5558) {
                    //pk gifts succeeds.
                    let respMsg = JSON.parse(respData.data);
                    console.log(respMsg);
                    this.playGiftSvga(respMsg.url);
                    this.changeBarWidth(respMsg.pkNum);

                    //PK Results
                    if (respMsg.winnerId != null) {
                        this.pkEnd = true;
                        this.$message.success("Congrats to Anchor " + respMsg.winnerId+" Won This PK!");
                        return;
                    } 
                } else if (respData.bizCode == 5559) {
                    
                    let respMsg = JSON.parse(respData.data);
                    this.pkObjId = respMsg.pkObjId;
                    this.$message.success("pk user online");
                    this.pkObjImg = respMsg.pkObjAvatar;
                }
                this.sendAckCode(respData);
            }
        },

        changeBarWidth: function (pkNum) {
            console.log(pkNum);
            let pkBar = document.getElementById("pkBar");
            pkBar.style.width = (pkNum + '%');
        },

        sendAckCode: function (respData) {
            let jsonStr = { "userId": this.initInfo.userId, "appId": 10001, "msgId": respData.msgId };
            let bodyStr = JSON.stringify(jsonStr);
            let ackMsgStr = { "magic": 19231, "code": 1005, "len": bodyStr.length, "body": bodyStr };
            this.websocketSend(JSON.stringify(ackMsgStr));
        },

        websocketSend: function (data) {//send websocket message
            this.websock.send(data);
        },

        websocketClose: function (e) {  //ends ws connection
            window.alert("The connection is ended for being idle! Please refresh this page!");
            console.log('Connection Ends!', e);
        },

        startHeartBeatJob: function () {
            console.log('Login Success!');
            let that = this;
            //send heartbeat packets
            let jsonStr = { "userId": this.initInfo.userId, "appId": 10001 };
            let bodyStr = JSON.stringify(jsonStr);
            let heartBeatJsonStr = { "magic": 19231, "code": 1004, "len": bodyStr.length, "body": bodyStr };
            setInterval(function () {
                that.websocketSend(JSON.stringify(heartBeatJsonStr));
            }, 3000);
        },

        closeLivingRoom: function () {
            let data = new FormData();
            data.append("roomId", getQueryStr("roomId"));
            httpPost(closeLiving, data)
                .then(resp => {
                    console.log(resp);
                    if (isSuccess(resp)) {
                        window.location.href = './live_stream_room_list.html';
                    }
                });
        },


        sendReview: function () {
            if (this.form.review == '') {
                this.$message({
                    message: "Comment should not be empty",
                    type: 'warning'
                });
                return;
            }
            let sendMsg = { "content": this.form.review, "senderName": this.initInfo.watcherNickName, "senderImg": this.initInfo.watcherAvatar };
            let msgWrapper = { "msgType": 1, "msg": sendMsg };
            this.chatList.push(msgWrapper);
            //Send comments to IM server
            let msgBody = { "roomId": this.roomId, "type": 1, "content": this.form.review, "senderName": this.initInfo.watcherNickName, "senderAvtar": this.initInfo.watcherAvatar };
            console.log(this.initInfo);
            let jsonStr = { "userId": this.initInfo.userId, "appId": 10001, "bizCode": 5555, "data": JSON.stringify(msgBody) };
            let bodyStr = JSON.stringify(jsonStr);
            console.log('Send Message');
            let reviewMsg = { "magic": 19231, "code": 1003, "len": bodyStr.length, "body": bodyStr };
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
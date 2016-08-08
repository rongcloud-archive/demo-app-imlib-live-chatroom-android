## Android 直播聊天室Demo说明
融云直播聊天室Demo 使用了IMLib SDK和第三方的播放SDK。前者提供聊天消息相关的基础服务，后者则负责视频播放控制。开源部分主要为UI 的绘制及SDK 的使用控制逻辑。

IMLib 是不含界面的基础IM 通讯能力库，封装了通信能力和会话、消息等对象。引用到工程中后，需要开发者自己实现UI 界面，相对较轻量，适用于对UI 有较高订制需求的开发者。

直播聊天室Demo：[点击下载](https://github.com/rongcloud/demo-app-imlib-live-chatroom-android)

### IMLib SDK 集成说明
1. 下载IMLib SDK：[点击下载](http://www.rongcloud.cn/downloads)

2. SDK 目录结构说明:
	* libs 目录 - native so库，目前支持armeabi（含v7及64位），以及x86平台。如有其它平台需求，请联系客服。
	* res 目录- 配置及资源文件。
	* build.gradle - gradle 配置相关，可再此定义编译版本等信息。
	* AndroidManifest.xml - SDK 配置文件，可查看SDK所需的安卓权限等信息。

3. 导入IMLib SDK：
	* 将下载的IMLib文件夹拷贝到工程根目录，为方便使用可更名为imlib。
	* 在settings.gradle 里加入imlib 库引用，如：

			include ':app', ':imlib'
	* 在主工程build.gradle 文件的dependencies 中加入imlib 库引用，如：
	
			compile project(':imlib')

至此IMLib SDK集成完毕，可在主工程里直接调用其接口。

### IMLib SDK 使用说明
1. SDK 初始化：

	在使用IMLib 接口前，需要对其做初始化操作，只需调一次即可。建议在继承Application类的onCreate 方法中调用。 接口说明如下：

		/**
		* @param context 传入Application类的Context。
		* @param appKey  融云注册应用的AppKey。
		*/
		public static void init(Context context, String appKey);

2. 注册消息监听：

	在连接融云服务器前，需要先注册消息接收监听。这里需要注意，如果在连接之后才注册监听，可能会有消息遗漏的情况。可以使用如下接口注册：
	
		/**
		* 设置接收消息事件的监听器。所有接收到的消息、通知、状态都经由此处设置的监听器处理。包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
		* 注意：如果调用此接口的Activity被释放回收，将无法收到事件回调。
		*
		* @param listener 接收消息的监听器。
		*/
		public static void setOnReceiveMessageListener(final OnReceiveMessageListener listener);

3. 获取Token：

	Token 即用户令牌，相当于您 APP 上当前用户连接融云的身份凭证。
	
	在您连接融云服务器之前，您需要请求您的 App Server，您的 App Server 通过 Server API 获取 Token 并返回给您的客户端，客户端获取到这个 Token 即可进入下一步连接融云服务器。

	为什么必须在服务器端请求 Token，客户端不提供获取 Token 的接口？
因为获取 Token 时需要提供 App Key 和 App Secret 。如果在客户端请求 Token，假如您的 App 代码一旦被反编译，则会导致您的 App Key 和 App Secret 泄露。所以，务必在您的服务器端获取 Token。

	我们在开发者控制台提供了 API 调试的功能，在开发初期阶段，您可以通过其中获取 Token 功能，手动获取 Token 进行测试。
	
4. 登录连接：
	
	当成功得到Token后，可以调用connect 接口与融云服务器进行连接。
	
		/**
		* 连接服务器，在整个应用程序全局，只需要调用一次，需在init之后调用。
		* 如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
		* 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。
		*
		* @param token    从服务端获取的用户身份令牌（Token）。
		* @param callback 连接回调。
		* @return RongIMClient IM 客户端核心类的实例。
		*/
		public static RongIMClient connect(final String token, final ConnectCallback callback);
    
    当回调onSuccess 代表连接成功；若回调onTokenIncorrect 很可能是Token跟 AppKey不匹配，可以去官网-我的应用-运营工具-用户管理 中检查确认；若返回onError 请根据错误码进行检查。
 
5. 加入聊天室：

	登录成功后，当用户点击房间进入直播聊天室需要调用如下接口，打开消息通道：
    
    	/**
		* 加入聊天室。如果聊天室不存在，sdk 会创建聊天室并加入，如果已存在，则直接加入。加入聊天室时，可以选择拉取聊天室消息数目。
		*
		* @param chatroomId      聊天室 Id。
		* @param defMessageCount 进入聊天室拉取消息数目，-1 时不拉取任何消息，0 时拉取 10 条消息，最多只能拉取 40 条。
		* @param callback        状态回调。
		*/
    	public void joinChatRoom(final String chatroomId, final int defMessageCount, final OperationCallback callback);
    
    或：
    
    	/**
		* 加入已存在的聊天室。如果聊天室不存在，则加入失败。加入聊天室时，可以选择拉取聊天室消息数目。
		*
		* @param chatroomId      聊天室 Id。
		* @param defMessageCount 进入聊天室拉取消息数目，-1 时不拉取任何消息，0 时拉取 10 条消息，最多只能拉取 40 条。
		* @param callback        状态回调。
		*/
    	public void joinExistChatRoom(final String chatroomId, final int defMessageCount, final OperationCallback callback);
    	
    如果聊天室不存在则返回错误码 23410，若人数超限则返回错误码 23411。

6. 发送消息：

		/**
		* 发送消息。
		*
		* @param message     将要发送的消息体。
		* @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。聊天室消息一般不需要推送，可设置为null。
		* @param pushData    push 附加信息。聊天室消息一般不需要推送，可设置为null。  
		* @param callback    发送消息的回调，参考IRongCallback.ISendMessageCallback。
		*/
    	public void sendMessage(final Message message, final String pushContent, final String pushData, final IRongCallback.ISendMessageCallback callback);

7. 用户信息显示：

	用户信息由UserInfo 类来承载，包含用户id，昵称，头像三部分。聊天室由于用户流动性大，建议将用户信息写入发送的消息体中。调用MessageContent的接口：
	
		/**
		* 将用户信息写入消息体内
		*
		* @param info 要写入的用户信息。
		*/
		public void setUserInfo(UserInfo info);
    
	当收到信息后，通过调用getUserInfo，得到UserInfo类，进而得到发送者的信息。

		/**
		* 获取消息体内的用户信息。
		*/
		public UserInfo getUserInfo()
    
8. 退出聊天室：

	当用户退出房间，需要调用如下接口：
	
		/**
		* 退出聊天室。
		*
		* @param chatroomId 聊天室 Id。
		* @param callback   状态回调。
		*/
    	public void quitChatRoom(final String chatroomId, final OperationCallback callback)；
    
9. 断开连接：

	通常直播聊天室应用，退出后不需要接受离线消息。调用下面接口即可：
	
		/**
		* 断开与融云服务器的连接，并且不再接收Push 消息。
		* 若想断开连接后仍然接受 Push 消息，可以调用disconnect()
		*/
		public void disconnect();

### 自定义消息相关
除了IMLib 内建的几种消息外，当有特殊需求时也可以自定义消息，基本概念请参看[文档](http://www.rongcloud.cn/docs/android_imlib.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%B6%88%E6%81%AF)。需要通过如下接口注册才能使用。

	/**
	* 用于自定义消息的注册, 注册后方能正确识别自定义消息, 建议在init后及时注册，保证自定义消息到达时能正确解析。
	*
	* @param messageContentClass 消息类型，必须要继承自 {@link io.rong.imlib.model.MessageContent}。
	* @throws AnnotationNotFoundException 如果没有找到注解时抛出。
	*/
	public static void registerMessageType(Class<? extends MessageContent> messageContentClass);

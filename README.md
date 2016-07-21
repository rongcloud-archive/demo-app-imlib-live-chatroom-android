### 下载地址
[http://www.rongcloud.cn/live](http://www.rongcloud.cn/live).

### 源码说明
    直播聊天室 SDK 是在融云 IMLib 库基础上添加了输入框控件，绝大接口操作同 IMLib，相关文档请访问[http://www.rongcloud.cn/docs/android_imlib.html](http://www.rongcloud.cn/docs/android_imlib.html)。
    Demo 源码默认集成了金山视频播放器，并对其进行了封装（ksplayer 目录），如使用其他直播平台请自行替换。Demo 只是作为直播聊天室的播放端使用，主播端则需要根据您选择的直播平台提供的方案来实现。
    Demo 登录页面 LoginActivity 中登录融云的操作，为了演示直接在客户端请求融云的服务器获取 token 然后登录，实际开发中需要通过您的服务器来融云获取 token，这样更安全，不易泄露您的 APP 的 APPSECRET 和 APPKEY（如需替换，请修改 HttpUtil.java 中的 APP_KEY 和 APP_SECRET）。

### Demo说明
    RongIM 这个类是对 IMLib 库的一层封装，以便于直播聊天室的使用，您也可以自己来实现这层逻辑。当连接融云服务器成功后，通过RongIM.getInstance().startConversation 接口来启动聊天室，最后两个参数分别是聊天室id，以及直播源url。
    Demo 里给出了一种 UI 实现方式，在 LiveChatRoomFragment 里通过 LiveView 来显示聊天记录。在 template 目录里，封装了 TextMessage 的显示模板（TextMessageTemplate.java）。如果需要使用其他种类消息，可以仿照 TextMessageTemplate.java 自行封装显示。
    同时 Demo 里还自定义了一种消息类型（GiftMessage，及对应显示模板GiftMessageTemplate），需要通过 registerMessageTemplate 接口注册后才能使用。自定义消息的更多资料，请查阅（http://www.rongcloud.cn/docs/android.html#消息自定义）(http://www.rongcloud.cn/docs/android.html#消息自定义)。

您认为此回答对您有帮助？

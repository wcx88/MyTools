rem 第一步、在命令提示符下输入命令：
netsh wlan set hostednetwork mode=allow  (如下图所示:)
rem 第二步、在命令提示符中输入：netsh wlan set hostednetwork ssid=您想要的无线网络的名称 key=您想要设置的密码
netsh wlan set hostednetwork ssid=wwwcx key=wcx.1234567890
rem 在命令提示符中继续输入：netsh wlan start hostednetwork   (这一步是打开刚才虚拟的无线网络，这样你的Wifi手机就可以链接到你刚才建的虚拟Wifi网络了)

netsh wlan start hostednetwork

rem pause

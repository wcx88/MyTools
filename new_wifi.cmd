rem ��һ������������ʾ�����������
netsh wlan set hostednetwork mode=allow  (����ͼ��ʾ:)
rem �ڶ�������������ʾ�������룺netsh wlan set hostednetwork ssid=����Ҫ��������������� key=����Ҫ���õ�����
netsh wlan set hostednetwork ssid=wwwcx key=wcx.1234567890
rem ��������ʾ���м������룺netsh wlan start hostednetwork   (��һ���Ǵ򿪸ղ�������������磬�������Wifi�ֻ��Ϳ������ӵ���ղŽ�������Wifi������)

netsh wlan start hostednetwork

rem pause

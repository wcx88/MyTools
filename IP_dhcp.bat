set st="��������"
netsh interface ip set address "%st%" dhcp
netsh interface ip set dns name="%st%" source=dhcp

ipconfig /release 
ipconfig /renew




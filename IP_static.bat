
set st="本地连接"
set addr="192.168.1.131"
set mask="255.255.240.0"
set gateway="192.168.0.254"
set dns_addr1="218.85.157.99"
set dns_addr2="218.85.152.99"

	netsh interface ip set address name="%st%" source=static addr=%addr% mask=%mask% >nul
	netsh interface ip set address name="%st%" gateway=%gateway% gwmetric=0 >nul
	netsh interface ip set dns name="%st%" source=static addr=%dns_addr1% register=PRIMARY >nul
	netsh interface ip add dns name="%st%" addr=%dns_addr2% >nul

rem netsh interface ip set address "本地连接" static %addr% %mask% %gateway%
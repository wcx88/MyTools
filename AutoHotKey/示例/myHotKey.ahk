
;ctrl + p 改成 向上
*<^p::
SendEvent {up down}
KeyWait p  ; 防止键盘自动重复导致重复.
SendEvent {up up}
return

;ctrl + n 改成向下
*<^n::
SendEvent {down down}
KeyWait n  ; 防止键盘自动重复导致重复.
SendEvent {down up}
return

;ctrl + b 改成向左
*<^b::
SendEvent {left down}
KeyWait b  ; 防止键盘自动重复导致重复.
SendEvent {left up}
return

;ctrl + f 改成向右
*<^f::
SendEvent {right down}
KeyWait f  ; 防止键盘自动重复导致重复.
SendEvent {right up}
return



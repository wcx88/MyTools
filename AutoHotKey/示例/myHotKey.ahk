
;ctrl + p �ĳ� ����
*<^p::
SendEvent {up down}
KeyWait p  ; ��ֹ�����Զ��ظ������ظ�.
SendEvent {up up}
return

;ctrl + n �ĳ�����
*<^n::
SendEvent {down down}
KeyWait n  ; ��ֹ�����Զ��ظ������ظ�.
SendEvent {down up}
return

;ctrl + b �ĳ�����
*<^b::
SendEvent {left down}
KeyWait b  ; ��ֹ�����Զ��ظ������ظ�.
SendEvent {left up}
return

;ctrl + f �ĳ�����
*<^f::
SendEvent {right down}
KeyWait f  ; ��ֹ�����Զ��ظ������ظ�.
SendEvent {right up}
return



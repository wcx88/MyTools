
*#up::MouseMove, 0, -10, 0, R  ; Win+UpArrow �ȼ� => ���ƹ��
*#Down::MouseMove, 0, 10, 0, R  ; Win+DownArrow => ���ƹ��
*#Left::MouseMove, -10, 0, 0, R  ; Win+LeftArrow => ���ƹ��
*#Right::MouseMove, 10, 0, 0, R  ; Win+RightArrow => ���ƹ��

*<#RCtrl::  ; LeftWin + RightControl => Left-click (��ס Control/Shift ������ Control-Click �� Shift-Click).
SendEvent {Blind}{LButton down}
KeyWait RCtrl  ; ��ֹ�����Զ��ظ����µ��ظ������.
SendEvent {Blind}{LButton up}
return

*<#RShift::  ; LeftWin + RightShift => Right-click
SendEvent {Blind}{RButton down}
KeyWait RShift  ; ��ֹ�����Զ��ظ������ظ��������.
SendEvent {Blind}{RButton up}
return
a = Analysis([os.path.join(HOMEPATH,'support\\_mountzlib.py'), os.path.join(HOMEPATH,'support\\useUnicode.py'), 'CutImgFileHead.py'],
             pathex=['E:\\360\xd4\xc6\xc5\xcc\xd7\xd4\xb6\xaf\xcd\xac\xb2\xbd\xc4\xbf\xc2\xbc\\Q_\xc6\xe4\xcb\xfc\\MyTools\\CIF2\xd3\xb0\xcf\xf1\xce\xc4\xbc\xfe\xd7\xaa\xbb\xbb\xc6\xf7\\src'])
pyz = PYZ(a.pure)
exe = EXE(pyz,
          a.scripts,
          exclude_binaries=1,
          name='buildCutImgFileHead/CutImgFileHead.exe',
          debug=False,
          strip=False,
          upx=False,
          console=True )
coll = COLLECT( exe,
               a.binaries,
               strip=False,
               upx=False,
               name='distCutImgFileHead')

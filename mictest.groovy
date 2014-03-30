import jp.crestmuse.cmx.processing.*
import jp.crestmuse.cmx.amusaj.sp.*
import jp.crestmuse.cmx.math.*

def cmx = CMXController.getInstance()
cmx.readConfig("config.xml")
def mic = cmx.createMic()
def stft = cmx.createSTFT(false)
def m = cmx.newSPModule(inputs:[ComplexArray.class],
                        outputs:[], 
                        execute:{ src, dst ->
                          print(".")
                        })
cmx.addSPModule(m)
cmx.connect(mic, 0, stft, 0)
cmx.connect(stft, 0, m, 0)
cmx.startSP()

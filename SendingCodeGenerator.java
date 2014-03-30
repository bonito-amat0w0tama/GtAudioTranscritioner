import java.io.IOException;
import java.net.ConnectException;

import jp.crestmuse.cmx.math.DoubleMatrix;
import jp.crestmuse.cmx.amusaj.sp.*;


public class SendingCodeGenerator {
		public SendingCodeGenerator() {
		}
		
		public String putPrintCode() {
			return null;
		}

		public String makeNMFCode(String method , int base, int iter) {
			String sBase = String.valueOf(base);
			String sIter = String.valueOf(iter);
            String nmfCode =  
                    "V = server.pop()\n" +
            		"W, H, etc = server.nmfMatrix(V," + method + "," + sBase + "," + sIter + ")\n" +
                    "SW, SH = Utils.NMFUtils.sortBasisAndCoef(W, H)\n" +
                    "Wp = server.getPseudoInverseMatrix(SW)\n" +
                    "data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist(), 'SW': SW.tolist(), 'SH': SH.tolist(), 'etc': etc}\n";
            return nmfCode;
		}

		public String makePopCode() {
            String pop = "V = server.pop()\n";
            String data = "data={'V': V.tolist()}\n";
            String dest = pop + data;

            return dest;
		}	

		public String makeSaveCode(String name, boolean dateFlag) {
            String flag;
            if (dateFlag) {
            	flag = "True";
            } else {
            	flag = "False";
            }

            String write = "server.writeDataToJson(name=" + name + ", data=data, dateFlag=" + flag + ")\n"; 
            return write;
		}	

		// exec関数の参照範囲をglobalに変更したので,selfをserverに変更する必要があり
        String nmfCode = 
        		"V = self.pop()\n" +
        		"W,H = self.nmfMatrix(V, 'nmf', 10, 1000)\n" +
        		"self.push(W)\n" +
        		"self.push(H)\n" +
        		"Wp = self.getPseudoInverseMatrix(W)\n" +
        		"self.push(Wp)\n" +
                "self.pushMatrix(self.pop())\n" +
        		"self.pushMatrix(self.pop())\n" +
        		"self.pushMatrix(self.pop())\n" +
                "self.writeDataToJson(name='zenon', data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist()}, dateFlag=True)";

        String junkNmf = 
        		"V = server.pop()\n" +
        		"W,H = server.nmfMatrix(V, 'nmf', 10, 500)\n" +
//        		"Wp = server.getPseudoInverseMatrix(W)\n" +
        		"print W.shape\n" +
        		"print H.shape\n" +
//        		"server.push(W)\n" +
//        		"server.push(H)\n" +
//        		"Wp = server.getPseudoInverseMatrix(W)\n" +
//        		"server.push(Wp)\n" +
//                "server.pushMatrix(server.pop())\n" +
//        		"server.pushMatrix(server.pop())\n" +
//        		"server.pushMatrix(server.pop())\n" +
//              "server.writeDataToJson(name='zenon', data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist()}, dateFlag=True)\n" +
//				"X = np.arange(25).reshape(5, 5)\n" +
//				"path = '../../jsonData/zenon_2013-12-26-20:1.json'\n" +
//				"data = Utils.NMFUtils.readJson(path=path)\n" +
//				"data = Utils.NMFUtils.json2NpArray(data)\n" +
//				"Wd = data['W']\n" +
//        		"print Wd.shape\n" +
//				"x = np.arange(100)\n" +
//        		"print type(W)\n" +
//        		"W = np.asarray(W)\n" +
//        		"print type(W)\n" +
				"SW, SH = Utils.NMFUtils.sortBasisAndCoef(W, H)\n" +
        		"Wp = server.getPseudoInverseMatrix(SW)\n" +
        		"print Wp.shape\n" +
        		"print V.shape\n" +
        		"h = np.dot(Wp, V[:,135])\n" +
        		"print h.shape\n" +
        		"a = range(10)\n" +
        		"print np.max(h)\n" +
//        		"plt.plot(h)\n" +
        		"print server.u.printMaxIndex(h)\n" +
//        		"Utils.NMFUtils.createActivationGraph(h, 1)\n" +
//        		"Utils.NMFUtils.createCoefGraph(SH, 2)\n" +
        		"Utils.NMFUtils.createBasisGraph(SW, 1)\n" +
        		"Hd = np.dot(Wp, V)\n" +
        		"Utils.NMFUtils.createCoefGraph(Hd, 2)\n" +
//        		"Utils.NMFUtils.plotTest(x=x)\n" +
                "server.writeDataToJson(name='doremi', data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist()}, dateFlag=True)\n" +
        		"plt.show()\n";
//        		"Utils.NMFUtils.showPlot()";
        
        String pilot = 
        		"V = server.pop()\n" +
        		"W,H = server.nmfMatrix(V, 'nmf', 10, 500)\n" +
				"SW, SH = Utils.NMFUtils.sortBasisAndCoef(W, H)\n" +
        		"Wp = server.getPseudoInverseMatrix(SW)\n" +
        		"server.setMatrix(data=V, name='V')\n" +
        		"server.setMatrix(data=SW, name='SW')\n" +
				"server.setMatrix(data=SH, name='SH')\n" +
				"server.setMatrix(data=Wp, name='Wp')\n" +
        		"server.sendMatrix(server.takeMatrix(name='V'))\n" +
        		"server.sendMatrix(server.takeMatrix(name='SW'))\n" +
        		"server.sendMatrix(server.takeMatrix(name='SH'))\n" +
        		"server.sendMatrix(server.takeMatrix(name='Wp'))\n" +
        		"print nl.norm(np.dot(SW, SH))\n" +
        		"print nl.norm(np.dot(W, H))\n" +
        		"data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist(), 'SW': SW.tolist(), 'SH': SH.tolist()}\n" +
                "server.writeDataToJson(name='doremi', data=data, dateFlag=True)\n"; 
//                "server.writeDataToJson(name='doremi', data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist(), 'SW': SW.toList(), 'SH: SH.tolist()}, dateFlag=True)"; 
        
        String pilotTrans = 
//        		"path = '../../jsonData/doremi_normal_2014-1-6-14:31.json'\n" +
        		"path = '../../jsonData/am_2014-1-7-21:36.json'\n" +
//        		"path = '../../jsonData/st/5st_2014-1-10-20:54.json'\n" +
//        		"path = '../../jsonData/st/0210/d3st.json'\n" +
        		"data = server.nu.readJson(path=path)\n" +
        		"data = server.nu.json2NpArray(data)\n" +
        		"V = data['V']\n" +
        		"SW = data['SW']\n" +
        		"SH = data['SH']\n" +
        		"Wp = data['Wp']\n" +
        		"server.sendMatrix(V)\n" +
        		"server.sendMatrix(SW)\n" +
        		"server.sendMatrix(SH)\n" +
        		"server.sendMatrix(Wp)\n";
        
        String nmfZenon= 
        		"V = server.pop()\n" +
        		"W, H = server.nmfMatrix(V, 'nmf', 84, 4000)\n" +
				"SW, SH = Utils.NMFUtils.sortBasisAndCoef(W, H)\n" +
        		"Wp = server.getPseudoInverseMatrix(SW)\n" +
        		"data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist(), 'SW': SW.tolist(), 'SH': SH.tolist()}\n" +
                "server.writeDataToJson(name='AmScalw', data=data, dateFlag=True)\n"; 
        		
        String nmfAm = 
        		"V = server.pop()\n" +
        		"W, H  = server.nmfMatrix(V, 'nmf', 17, 2000)\n" +
				"SW, SH = Utils.NMFUtils.sortBasisAndCoef(W, H)\n" +
        		"Wp = server.getPseudoInverseMatrix(SW)\n" +
//        		"server.sendMatrix(V)\n" +
//        		"server.sendMatrix(SW)\n" +
//        		"server.sendMatrix(SH)\n" +
//        		"server.sendMatrix(Wp)\n" +
        		"data={'V': V.tolist(), 'W': W.tolist(), 'H': H.tolist(), 'Wp': Wp.tolist(), 'SW': SW.tolist(), 'SH': SH.tolist()}\n" +
                "server.writeDataToJson(name='am', data=data, dateFlag=True)\n"; 
        
        String saveMat = 
        		"V = server.pop()\n" +
        		"data={'V': V.tolist()}\n" +
                "server.writeDataToJson(name='ghorst', data=data, dateFlag=True)\n"; 

        String transSt6 = 
//        		"path = '../../jsonData/st/0210/d3st.json'\n" +
        		"path = '../../jsonData/st/0210/6st.json'\n" +
        		"data = server.nu.readJson(path=path)\n" +
        		"data = server.nu.json2NpArray(data)\n" +
//        		"V = data['V']\n" +
//        		"SW = data['SW']\n" +
//        		"SH = data['SH']\n" +
        		"Wp = data['Wp']\n" +
//        		"server.sendMatrix(V)\n" +
//        		"server.sendMatrix(SW)\n" +
//        		"server.sendMatrix(SH)\n" +
        		"server.sendMatrix(Wp)\n";

        String transSt1 = 
        		"path = '../../jsonData/st/0210/d1st.json'\n" +
        		"data = server.nu.readJson(path=path)\n" +
        		"data = server.nu.json2NpArray(data)\n" +
        		"Wp = data['dWp']\n" +
        		"server.sendMatrix(Wp)\n";
        String transSt2 = 
        		"path = '../../jsonData/st/0210/d2st.json'\n" +
        		"data = server.nu.readJson(path=path)\n" +
        		"data = server.nu.json2NpArray(data)\n" +
        		"Wp = data['dWp']\n" +
        		"server.sendMatrix(Wp)\n";
        String trans = 
        		"path1 = '../../jsonData/st/0210/d1st.json'\n" +
        		"path2 = '../../jsonData/st/0210/d2st.json'\n" +
        		"path3 = '../../jsonData/st/0210/d3st.json'\n" +
        		"path4 = '../../jsonData/st/0210/d4st.json'\n" +
        		"path5 = '../../jsonData/st/0210/d5st.json'\n" +
        		"path6 = '../../jsonData/st/0210/d6st.json'\n" +
        		"st1= server.nu.readJson(path=path1)\n" +
        		"st1= server.nu.json2NpArray(st1)\n" +
        		"st2= server.nu.readJson(path=path2)\n" +
        		"st2= server.nu.json2NpArray(st2)\n" +
        		"st3= server.nu.readJson(path=path3)\n" +
        		"st3= server.nu.json2NpArray(st3)\n" +
        		"st4= server.nu.readJson(path=path4)\n" +
        		"st4= server.nu.json2NpArray(st4)\n" +
        		"st5= server.nu.readJson(path=path5)\n" +
        		"st5= server.nu.json2NpArray(st5)\n" +
        		"st6= server.nu.readJson(path=path6)\n" +
        		"st6= server.nu.json2NpArray(st6)\n" +
        		"Wp1 = st1['dWp']\n" +
        		"Wp2 = st2['dWp']\n" +
        		"Wp3 = st3['dWp']\n" +
        		"Wp4 = st4['dWp']\n" +
        		"Wp5 = st5['dWp']\n" +
        		"Wp6 = st6['dWp']\n" +
        		"server.sendMatrix(Wp1)\n" +
        		"server.sendMatrix(Wp2)\n" + 
        		"server.sendMatrix(Wp3)\n" +
        		"server.sendMatrix(Wp4)\n" +
        		"server.sendMatrix(Wp5)\n" +
        		"server.sendMatrix(Wp6)\n";
        String trans0212 = 
        		"path1 = '../../jsonData/st/0212/d1st.json'\n" +
        		"path2 = '../../jsonData/st/0212/d2st.json'\n" +
        		"path3 = '../../jsonData/st/0212/d3st.json'\n" +
        		"path4 = '../../jsonData/st/0210/d4st.json'\n" +
        		"path5 = '../../jsonData/st/0210/d5st.json'\n" +
        		"path6 = '../../jsonData/st/0210/d6st.json'\n" +
        		"st1= server.nu.readJson(path=path1)\n" +
        		"st1= server.nu.json2NpArray(st1)\n" +
        		"st2= server.nu.readJson(path=path2)\n" +
        		"st2= server.nu.json2NpArray(st2)\n" +
        		"st3= server.nu.readJson(path=path3)\n" +
        		"st3= server.nu.json2NpArray(st3)\n" +
        		"st4= server.nu.readJson(path=path4)\n" +
        		"st4= server.nu.json2NpArray(st4)\n" +
        		"st5= server.nu.readJson(path=path5)\n" +
        		"st5= server.nu.json2NpArray(st5)\n" +
        		"st6= server.nu.readJson(path=path6)\n" +
        		"st6= server.nu.json2NpArray(st6)\n" +
        		"Wp1 = st1['dWp']\n" +
        		"Wp2 = st2['dWp']\n" +
        		"Wp3 = st3['dWp']\n" +
        		"Wp4 = st4['dWp']\n" +
        		"Wp5 = st5['dWp']\n" +
        		"Wp6 = st6['dWp']\n" +
        		"server.sendMatrix(Wp1)\n" +
        		"server.sendMatrix(Wp2)\n" + 
        		"server.sendMatrix(Wp3)\n" +
        		"server.sendMatrix(Wp4)\n" +
        		"server.sendMatrix(Wp5)\n" +
        		"server.sendMatrix(Wp6)\n";
        
        String cut = 
        		"cut = server.pop()\n" +
//        		"self.push(cut)\n" +
//        		"self.pushMatrix(self.pop())\n" +
                "server.writeDataToJson(name='cut', data={'cut': cut.tolist()}, dateFlag=False)";
//                "server.writeDataToJson(name='am', data=data, dateFlag=True)\n"; 
        
        public static void main(String[] args) {
        	SendingCodeGenerator scg = new SendingCodeGenerator();
            GuitarAllNoteAnalyzer gaa = new GuitarAllNoteAnalyzer();

//            String path = "./data/guitar.wav";
//            String path = "./data/AmScale_0108.wav"; //            String path = "../data/zenon_cut_0109_6st.wav";
//            String path = "../data/5st_0109.wav";

            DoubleMatrix V = null, SH = null, SW =null, Wp = null;
            try {
            	int g1 = 2221;
            	int g2 = 22222;
            	int g3 = 2223;
            	int g4 = 2224;
            	int g5 = 2225;
            	int g6 = 2226;
            	int am = 11111;

//            	int g1 = 22221;
//            	int g2 = 111112;
//            	int g3 = 22223;
//            	int g4 = 22224;
//            	int g5 = 22225;
//            	int g6 = 22226;
//            	int am = 11111;

                ExternalCodeAdapter eca = 
//                    new ExternalCodeAdapter("localhost", 1111);
//                    new ExternalCodeAdapter("localhost", g6);
//                    new ExternalCodeAdapter("localhost", am);
//                    new ExternalCodeAdapter("192.168.11.207", 1111);
//                    new ExternalCodeAdapter("192.168.11.207", g6);
//                    new ExternalCodeAdapter("192.168.11.208", g6);
                    new ExternalCodeAdapter("localhost", 1111);
//                String path = "../data/1st_0110.wav";
//                String path = "data/short/6st_0213.wav";
                String path = "data/amCutting_0107.wav";
                //String path = "data/Am_0114.wav";
				DoubleMatrix allNote = gaa.analyzeGuitarAudio(path);
                eca.pushDoubleMatrix(allNote);

                // メモリの解放
                allNote = null;

//                int base = 46;
                int base = 28;
//                int iter = 10000;
                int iter = 30000;
//                int iter = 609;
//                int iter = 100;
                String code = scg.makeNMFCode("'nmf'", 84, 1) + scg.makeSaveCode("'AmScale'", true);
                String zenonCode = scg.makeNMFCode("'nmf'", 126, 1) + scg.makeSaveCode("'zenon'", true);

                String cut1Code = scg.makeNMFCode("'nmf'", base, iter) + scg.makeSaveCode("'1st'", true);
                String cut2Code = scg.makeNMFCode("'nmf'", base, iter) + scg.makeSaveCode("'2st'", true);
                String cut3Code = scg.makeNMFCode("'nmf'", base, iter) + scg.makeSaveCode("'3st'", true);
                String cut4Code = scg.makeNMFCode("'nmf'", base, iter) + scg.makeSaveCode("'4st'", true);
//                String cut5Code = scg.makeNMFCode("'nmf'", 30, 10000) + scg.makeSaveCode("'5st'", true);
                String cut5Code = scg.makeNMFCode("'nmf'", base, iter) + scg.makeSaveCode("'5st'", true);
                String cut6Code = scg.makeNMFCode("'nmf'", base, iter) + scg.makeSaveCode("'6st'", true);

                String a = scg.makeNMFCode("'nmf'", 19, 600) + scg.makeSaveCode("'am'", true);
                
                String cut = scg.cut;

                eca.pushCode(cut);

//                V = (DoubleMatrix)eca.pop();
//                SW = (DoubleMatrix)eca.pop();
//                SH = (DoubleMatrix)eca.pop();
//                Wp = (DoubleMatrix)eca.pop();

                eca.pushEnd();
                eca.close();
            } catch(ConnectException e) {
                System.out.println("Pythonサーバーとのコネクションエラー");
                e.printStackTrace();
                System.exit(-1);
            } catch(IOException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (InterruptedException e) {
				e.printStackTrace();
                System.exit(-1);
			}
        }
}

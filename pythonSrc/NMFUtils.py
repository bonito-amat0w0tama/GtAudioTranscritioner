import datetime
import os
import json
import traceback

@staticmethod
def writeDataToJson(self, name, data, dateFlag=True):
    try:
        if dateFlag:
            date = datetime.datetime.today()
            dateStr = str(date.year) + "-" + str(date.month) + "-" +str(date.day) + "-" + str(date.hour) + ":" + str(date.minute)
            filePath = "../../jsonData/" + name + "_" + dateStr + ".json"
        else:
            filePath = "../../jsonData/" + name + ".json" 

        # ファイルが存在しない場合のみ、Jsonファイルを生成
        if not os.path.isfile(filePath):
            file = open(filePath, "w")
            json.dump(data, file)
            file.close()
            print "Writing_josn_Succeed"
        else:
            print "File_exists"

    except Exception as e:
        print str(e)
        print type(e)
        traceback.print_exc()

@Grapes(
    @Grab(group='org.jsoup', module='jsoup', version='1.6.2')
)
import org.jsoup.*
import org.jsoup.nodes.*
import org.jsoup.select.*
import java.util.*;
import java.text.SimpleDateFormat
//import hudson.plugins.git.*
import hudson.Util;
import groovy.json.JsonSlurper;


node (label: 'slave1') {
def totalpassed = 0
def totalfailed = 0
def totalnontexecuted = 0
def totaltests = 0
def totalbugs = 0
def date = new Date()
def date_format=new SimpleDateFormat("EEE dd MMM HH:mm:ss z")
def build_date =date_format.format(date) 
def gitBranch='master'
def imageName= 'restassured'
List testArray = new ArrayList<Map<String,String>>();

    withMaven(maven:'maven') {
  env.gitBranch=gitBranch
  env.imageName = imageName
	  env.Mode="${params.modes}"
  try{ 
        stage('Checkout') {
            git url: 'http://git.thinkpalm.info/ATBAAT10/A-to-Be.git', credentialsId: 'a-to-be-01', branch: 'master'     
        }
		stage('Build') {
		    sh 'mvn package shade:shade'
            def pom = readMavenPom file:'pom.xml'
            env.version = pom.version
        }
        stage('Image') {
                sh 'docker stop restassured || true && docker rm restassured || true'
                cmd = "docker rmi restassured:${env.version} || true"
                sh cmd
                docker.build "restassured:${env.version}"
        }
        stage ('Run') {

			print "${params}"
			if ("${params.modes}" == "DRY_RUN") {
       			 sh "docker run -p 8081:8081 -h restassured --name restassured --net host -m=500m restassured:${env.version} DRY_RUN"
      	     }

      	     else if("${params.modes}" == "RUN") {
	  	 	 	 sh "docker run -p 8081:8081 -h restassured --name restassured --net host -m=500m restassured:${env.version} RUN"
      	     }
      	     else if("${params.modes}" == "FULL_RUN") {
	  	 		 sh "docker run -p 8081:8081 -h restassured --name restassured --net host -m=500m restassured:${env.version} FULL_RUN"
      	     }            

			sh "docker cp restassured:/test-output ."
	
	print "artifacts creation"	
// Archive the build output artifacts.
  if ("${params.modes}" == "DRY_RUN") 
    archiveArtifacts artifacts: 'test-output/emailable-report.html,test-output/*.txt'
	else
	archiveArtifacts artifacts: 'test-output/*report.html'	
    print "artifacts creation ends"			

	//def currentDir = new File("").getAbsolutePath() //for windows
	pathCommand= pwd	// for linux
	def currentDir = "$pathCommand";// for linux
	def filename = 'emailable-report.html'
	if ("${params.modes}" != "DRY_RUN")
			filename = 'xray_report.html'
	print "${currentDir}/jobs/${env.JOB_NAME}/builds/${env.BUILD_NUMBER}/archive/test-output/${filename}"
	def file1 = new File("${currentDir}/jobs/${env.JOB_NAME}/builds/${env.BUILD_NUMBER}/archive/test-output/${filename}")	
		print "file exists = ${file1.exists()}"
		
//file operation starts      
print "masterPage ${file1}"
Document doc = Jsoup.parse(file1, "UTF-8");

 	
	  if ("${params.modes}" == "DRY_RUN") {
	  
	  def jsonObj = readJsonFromFile(currentDir,env)
	  
    for (Element table : doc.getElementById("summary")) {       
        for (Element row : table.select("tr.passedeven")) {
            Elements tds = row.select("td");  
			Map<String,String> map1  = new HashMap<>();			
            totalpassed=totalpassed+1			
			if (tds.size() == 4) {  				
			   // map1.put("name", tds.get(1).text());
				 map1.put("name",getTesCaseDisplaName(jsonObj,  tds.get(1).text()));
            }
            else if (tds.size() == 3) {                
               // print "doc ${tds.get(0).text()}"			
			//	map1.put("name", tds.get(0).text()); 
				map1.put("name",getTesCaseDisplaName(jsonObj,  tds.get(0).text()));
            }		
		    map1.put("url", "${env.BUILD_URL}/console"); 
		    map1.put("flag", "1"); 
			 map1.put("bug_id", ""); 
			testArray.add(map1);
        }
		for (Element row : table.select("tr.failedeven")) {
            Elements tds = row.select("td");  
			Map<String,String> map1  = new HashMap<>();
            totalfailed=totalfailed+1			
			if (tds.size() == 4) {    
			 //	map1.put("name", tds.get(1).text());  
				map1.put("name",getTesCaseDisplaName(jsonObj,  tds.get(1).text()));
            }
            else if (tds.size() == 3) {     
			 // map1.put("name", tds.get(0).text()); 
				map1.put("name",getTesCaseDisplaName(jsonObj,  tds.get(0).text()));
            }
			  map1.put("url", "${env.BUILD_URL}/console"); 
		    map1.put("flag", "2");
			map1.put("bug_id", "0"); 
			testArray.add(map1);
        }
    }
  }
  else  if ("${params.modes}" != "DRY_RUN")
  {	
    //for loop starts
    	for (Element table : doc.select("table")[5]) {       
        for (Element row : table.select("tr")) {		
		if(row != table.select("tr").first()){
            Elements tds = row.select("td"); 			
			Map<String,String> map1  = new HashMap<>();			           		
			 if (tds.size() == 3) {                
               // print "doc ${tds.get(2).text()}"	
				map1.put("name", tds.get(0).text()); 
				def status =tds.get(1).text();				
				if(status == 'PASS')
                   {
				    map1.put("flag", "1");
				    totalpassed = totalpassed+1	
				   }				   
                 else if(status == 'FAIL')
				 {
				    map1.put("flag", "2");
					totalfailed = totalfailed+1
					totalbugs = totalbugs+1
					Elements links = tds.get(2).select("a[href]");
					if(links.size()== 1)
					  map1.put("bug_url", links[0].attr("href")); 
			     }
				 else if(status == 'TODO')
				 {
				    map1.put("flag", "3");
					totalnontexecuted = totalnontexecuted+1					
					Elements links = tds.get(2).select("a[href]");
					if(links.size()== 1)
					  map1.put("bug_url", links[0].attr("href")); 
			     }				   
				map1.put("bug_id", tds.get(2).text()); 
            }
			if (tds.size() == 1) {  //heading			
				def status =tds.get(0).text();				
				map1.put("name", status); 
				map1.put("flag", "4");
			}		
		    map1.put("url", "${env.BUILD_URL}/console");
			testArray.add(map1);
	     }
        }
      }	
	//end of for loop
	
	if ("${params.modes}" == "RUN")
	        totalbugs = 0;	 
  }
	print "total passed ${totalpassed}"
	print "total failed ${totalfailed}"
//file operation ends
	}
	}catch(Exception){ 
	 currentBuild.result = 'FAILURE'
	}
      }
	stage('mail'){		    
			env.totalpassed=totalpassed
            env.totalfailed=totalfailed
			env.totalnontexecuted= totalnontexecuted
			env.totaltests=totalpassed + totalfailed + totalnontexecuted
			env.totalbugs = totalbugs
			env.build_date = build_date.toString()
			env.build_result = currentBuild.currentResult
			env.build_duration =  Util.getTimeSpanString(System.currentTimeMillis() - currentBuild.startTimeInMillis)
			print "build_duration: ${env.build_duration}"
		    
		 def config = [:]	
	def subject = config.subject ? config.subject : "${env.JOB_NAME} Build Test Report - ${currentBuild.currentResult}!"
       subject="A-to-Be CI Execution Report - ${params.modes}" ;
        // Attach buildlog when the build is not successfull
        def attachLog = (config.attachLog != null) ? config.attachLog : (currentBuild.currentResult != "SUCCESS")
		 def content=createTemplate(env,testArray)		 
         env.ForEmailPlugin = env.WORKSPACE
        emailext mimeType: 'text/html',
	attachLog :true,
	compressLog : true,  
	body:content,		
        subject: subject,        
        to: 'dhananjaya.k@thinkpalm.com'
		 }
    }



def readJsonFromFile(currentDir,env){
//json reading starts	
    
	"jsonObj reading ends" 
	def filepath = "${currentDir}/jobs/${env.JOB_NAME}/builds/${env.BUILD_NUMBER}/archive/test-output/JSON_DRYRUN.txt"
	print "filepath ${filepath}"	
	def jsonFile =new File(filepath)
	 def isFileExists =jsonFile.exists()// fileExists("JSON_DRYRUN.txt" ,filepath)
	jsonFile=null;
	print "jsonObject = ${isFileExists}"	 
    def jsonObj = null		
	   if(isFileExists){
	        print "mapping1 file exists" 	  
	        try{   
		   
		   def jsonSlurper = new JsonSlurper()
           jsonObj = jsonSlurper.parse(new File(filepath))  
	        }
	        catch(err){
	             print "jsonObj in error" 	  
	            print "${err}"
	        }
	   }
	  print "jsonObj reading ends" 
      return jsonObj	  
//json reading ends
}


//function to get display name from jsonObject
def getTesCaseDisplaName(jsonObj,  testCase) {
try{
  def displayName = testCase
    if(jsonObj!=null){
	  displayName = jsonObj[testCase]
	if(displayName == null)
	  {
	    displayName = testCase
	  }
	   print "jsonObj name = ${jsonObj[testCase]}"
	  print "diplay name = ${displayName}"
	  print "testcase = ${displayName}"	  	 
	}
	return displayName
}
catch(err){print"${err}"}
}



//function to create template
def createTemplate(env,  testArray) {
    def sb = new StringBuilder()
	sb.append '<!doctype html><html lang="en">'

sb.append '<head> <!-- Required meta tags --> <meta charset="utf-8"> <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> <title>A-to-Be</title> </head>'
sb.append '<body style="font-family: Arial; line-height: 1.4;font-size:10px"><table style="width:600px;font-family: Arial;font-size: 12px;" align="center"><tr><td><div style="width: 600px;margin: auto; font-family: Arial;">'

//images starts
sb.append  '<div style="padding-left:25px;"> <img data-imagetype="External" src="https://res.cloudinary.com/dbgakpfay/image/upload/v1567143406/Work/ato-bee-logo_trt9fh.jpg" ></div> <div>'
sb.append  '<div> <img data-imagetype="External" src="https://res.cloudinary.com/dbgakpfay/image/upload/v1567143417/Work/banner_ewqhw7.jpg" style="width: 100%"></div>'
//images ends

   //heading data starts
    sb.append '<h3 style="text-align: center; color: #0056ab;font-size:14px">A-to-Be CI Execution Report</h3>'   
  sb.append '<table style="width: 100%;font-family: Arial;font-size: 12px;"> <tr> <td style="width: 50%">'  
  sb.append '<table style="font-family: Arial;font-size: 12px; padding-left:0" cellpadding="3">'
  
  //BRANCH_NAME
  sb.append ' <tr> <th style="text-align: left;font-family: Arial;">Branch</th> <td style="color:#666">'
  sb.append env.gitBranch
   sb.append '</td></tr>'
   
    //image  
  sb.append ' <tr> <th style="text-align: left">Image</th> <td style="color:#666">'
  sb.append env.imageName
  sb.append '</td></tr>'
  
   //version  
  sb.append ' <tr> <th style="text-align: left">Image Version</th> <td style="color:#666">'
  sb.append env.version
  sb.append '</td></tr>'
    
   //Execution Mode  
  sb.append ' <tr> <th style="text-align: left">Execution Mode</th> <td style="color:#666">'
  sb.append env.Mode
  sb.append '</td></tr>'  
  
  sb.append '</table> </td> <td style="width: 50%" align="right">'  
  sb.append '<table style="font-family: Arial;;font-size: 12px;"  cellpadding="3">'
  
  //build Id
  sb.append ' <tr> <th style="text-align: left">Build Id</th> <td style="color:#666">'
  sb.append('<a href="'+env.BUILD_URL+'"'+' style="color:#666;text-decoration: none;"><b>#'+env.BUILD_NUMBER+'</b></a>') 
   sb.append '</td></tr>'
   
     //build_result = Build Status
  sb.append ' <tr> <th style="text-align: left"> Build Status</th> <td style="color:#666">'
  sb.append env.build_result
   sb.append '</td></tr>'
   
   //build_duration
  sb.append ' <tr> <th style="text-align: left">Test Duration</th> <td style="color:#666">'
  sb.append env.build_duration
   sb.append '</td></tr>'
   
     //build_date
  sb.append '<tr> <th style="text-align: left">Build Date</th> <td style="color:#666">'
  sb.append env.build_date
  sb.append '</td></tr>'
  
   sb.append '</table></td> </tr> </table>'
  //heading data ends
    
  sb.append '<h3 style=" color: #0056ab; margin-top: 20px;font-size:14px">Execution Summary</h3>'
  
  // Execution-Summary starts 
  sb.append '<table style="width: 100%; text-align: center;font-family: Arial;font-size: 12px;" cellpadding="3">' 
  sb.append '<tr style="text-transform: uppercase; font-size: 11px;"> <th style="border-bottom: 1px solid #eee; width: 108px">Total Test case </th> <th style="border-bottom: 1px solid #eee">Passed </th> <th style="border-bottom: 1px solid #eee">Failed </th> <th style="border-bottom: 1px solid #eee; width: 108px"> Bugs Reported / Reopened</th> </tr>'
  sb.append '<tr>'  
 sb.append '<tr>'   
   sb.append '<td style="border-bottom: 1px solid #eee;">' + env.totaltests + '</td>'
   sb.append '<td style="border-bottom: 1px solid #eee;color:#12a107">' + env.totalpassed + '</td>'
    sb.append '<td style="border-bottom: 1px solid #eee;color:#dd5050">' + env.totalfailed + '</td>'
    sb.append '<td style="border-bottom: 1px solid #eee;color:#dd5050">' + env.totalbugs + '</td>'	
sb.append '</tr></table>'	
// Execution-Summary ends

sb.append '<h3 style=" color: #0056ab; margin-top: 20px;font-size:14px">Test Case Execution Status</h3>'

//Test Case Execution Status
def StatusHeaddingText ='TEST CASE NAME'
if(env.Mode != 'DRY_RUN')
	StatusHeaddingText = 'TEST CASE ID'
sb.append ' <table style="width: 100%;font-family: Arial; text-align: left;font-size: 12px;" cellpadding="4"> <tr style="text-transform: uppercase; font-size: 11px;font-family: Arial;"> <th style="border-bottom: 1px solid #eee;width: 106px;text-align:left">'+ StatusHeaddingText +'</th> <th style="border-bottom: 1px solid #eee; text-align: center">Status </th> <th style="border-bottom: 1px solid #eee; width: 108px">Bugs Created / Reopened </th> </tr>'
   
   //for each loop starts   
    for (ArrayList<Map<String,String>> item : testArray) {  
	if(item.flag == "1"){//passed case	
	sb.append '<tr><td style="border-bottom: 1px solid #eee; padding-left: 20px;width: 320px;">' + item.name + '</td>'
	sb.append '<td style="border-bottom: 1px solid #eee;color:#12a107; text-align: center">'
	sb.append('<a href="'+item.url+'"'+' style="color:#12a107;text-decoration: none;">PASS</a>');	
	sb.append '</td><td style="border-bottom: 1px solid #eee;color:#12a107; text-align: center">'+ item.bug_id + '</td></tr>'	
	}  
	if(item.flag == "2"){//failed case	 
		 	sb.append '<tr><td style="border-bottom: 1px solid #eee; padding-left: 20px;width: 320px">' + item.name + '</td>'
	sb.append '<td style="border-bottom: 1px solid #eee;color:#dd5050; text-align: center">'
	sb.append('<a href="'+item.url+'"'+' style="color:#dd5050;text-decoration: none;">FAIL</a>');	
	sb.append '</td><td style="border-bottom: 1px solid #eee;color:#12a107; text-align: center">'
	if(item.bug_url != null)
	    sb.append('<a href="'+item.bug_url+'"'+' style="color:#dd5050;text-decoration: none;">'+item.bug_id+'</a>' + '</td></tr>')
	   else
	   {
	    def bugNo="";
	    if(item.bug_id != '0')
		    bugNo = item.bug_id
	    sb.append '<span style="border-bottom: 1px solid #eee;color:#000000; text-align: center">'+ bugNo + '</span></td></tr>'
	   }	
	}  
	if(item.flag == "3"){// Todo's		 
		 	sb.append '<tr><td style="border-bottom: 1px solid #eee; padding-left: 20px;width: 320px">' + item.name + '</td>'
	sb.append '<td style="border-bottom: 1px solid #eee;color:#000000; text-align: center">'
	sb.append('<a href="'+item.url+'"'+' style="color:#000000;text-decoration: none;">TODO</a>');	
	sb.append '</td><td style="border-bottom: 1px solid #eee;color:#000000; text-align: center">'+ item.bug_id + '</td></tr>'	
	}  
	if(item.flag == "4"){//Sub heading	
	sb.append '<tr><td style="font-size: 12px;"><b><u>' + item.name + '</u></b></td>'		
	}  	
}   
  //for each loop ends
	if(testArray.size == 0)
       sb.append  '<tr><td colspan="3" style="border:1px solid black;">No details available to show</td></tr>'
	
	sb.append ('</table></div></td></tr></table></body></html>')
	return sb.toString()
}

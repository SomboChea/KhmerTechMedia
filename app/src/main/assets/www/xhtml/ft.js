<!--
/*
by Dynamicdrive.com
*/
var line=new Array()
line[1]="កម្មវិធីអក្សរ​និយាយ​អង់គ្លេស "
line[2]="សំរាប់ជួយដល់ការបញ្ជេញសំឡេង"
line[3]="រ​បស់ពួកគេអោយកាន់តែប្រសើរឡើង"
line[4]="​សំរាប់សិស្សានុសិស្សដែលរៀនភាសាអង់គ្លេស​។"
line[5]="រៀបចំនិងបង្កើតដោយ៖  ជា សំបូរ​ ។"
line[6]="សារអេឡិចត្រូនិច៖  "
line[7]="admin@cheasombo.likesyou.org"
line[8]="sombochea100@gmail.com"
line[9]="cheasombo@outlook.com "
line[10]="លេខទូរស័ព្ទ៖ ​ +(855) 9662 94947​ ។"
line[11]="គាំទ្រដោយ: សាលា ILC Thmorsor"
line[12]="Developed by : SomboChea​ ។"

//
var ts_fontsize="15px"

//

var longestmessage=1
for (i=2;i<line.length;i++){
if (line[i].length>line[longestmessage].length)
longestmessage=i
}

//
var tscroller_width=line[longestmessage].length

lines=line.length-1 //

//
if (document.all||document.getElementById){
document.write('<form name="bannerform">')
document.write('<input type="text" name="banner" size="'+tscroller_width+'"')
document.write('  style="text-align:center; background-color:#f8f8f8; color: '+document.body.text+'; font-family: Kh Preyveng; font-size: '+ts_fontsize+'; font-weight:bold; border: medium none" onfocus="blur()">')
document.write('</form>')
}

temp=""
nextchar=-1;
nextline=1;
cursor="\\"
function animate(){
if (temp==line[nextline] & temp.length==line[nextline].length & nextline!=lines){
nextline++;
nextchar=-1;
document.bannerform.banner.value=temp;
temp="";
setTimeout("nextstep()",1000)}
else if (nextline==lines & temp==line[nextline] & temp.length==line[nextline].length){
nextline=1;
nextchar=-1;
document.bannerform.banner.value=temp;
temp="";
setTimeout("nextstep()",1000)}
else{
nextstep()}}

function nextstep(){

if (cursor=="\\"){
cursor="|"}
else if (cursor=="|"){
cursor="/"}
else if (cursor=="/"){
cursor="-"}
else if (cursor=="-"){
cursor="\\"}


nextchar++;
temp+=line[nextline].charAt(nextchar);
document.bannerform.banner.value=temp+cursor
setTimeout("animate()",120)}

//
if (document.all||document.getElementById)
window.onload=animate
// -->
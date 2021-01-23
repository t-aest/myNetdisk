export const Util = {
  formatdate :function (dateString,type){
    let date = new Date(dateString)
    let year = date.getFullYear();
    let month = addzero(date.getMonth() + 1);
    let weekday = addzero(date.getDate());
    let hour = addzero(date.getHours());
    let minute = addzero(date.getMinutes());
    let second = addzero(date.getSeconds());
    function addzero(value) {
      if(value<10){
        value="0"+value;
      }
      return value
    }
    if(type==1){
      return (year + "-" + month + "-" + weekday);
    }else{
      return (year + "-" + month + "-" + weekday+" "+hour+":"+minute+":"+second);
    }
  }
}

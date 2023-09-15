const isExists = document.querySelector('.isSuccess');
const record = document.querySelector('.record');
const isRecorded = document.querySelector('.recorded');
if (isExists!=null) {
    record.classList.add('open');
    window.scrollBy(0, record.getBoundingClientRect().y);
}
if(isRecorded!=null){
    window.scrollBy(0, isRecorded.getBoundingClientRect().y);
}
const recordBtn = document.getElementById('record-btn');
recordBtn.addEventListener('click', (evt) => {
    if(!record.classList.contains('open'))
        record.classList.add('open');
    else
        record.classList.remove('open');
})
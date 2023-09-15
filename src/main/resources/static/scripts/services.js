const filtered = document.querySelector('.filtered');
const table = document.querySelector('.table');
if(filtered!=null){
    window.scrollBy(0, table.getBoundingClientRect().y);
}
import Vue from 'vue'
const parseToDOM= function (str){
    var div = document.createElement("div");
    if(typeof str === "string")
        div.innerHTML = str;
    
    return div.childNodes;
 }
 const loadingHandle=function(el,binding){
    const html_str=`<div class="directive-loading we-loading we-loading--spinner" 
        style="width: 100%;
        text-align: center;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        background-color: rgba(255,255,255,.2);
        z-index: 900;">
        <span class="we-loading__spinner we-loading__spinner--spinner"
        style="
            display: inline-block;
            position: absolute;
            top: 50%;
            left:50%;
            margin:-0.4rem;
        "
        ><i></i><i></i><i></i><i></i><i></i><i></i><i></i><i></i><i></i><i></i><i></i><i></i></span></div>`
        let load_el= el.querySelectorAll(':scope>.directive-loading');
        if(binding.value&&!load_el.length){
            // console.log(parseToDOM(html_str))
            let node=parseToDOM(html_str);
            // console.log(node[0])
            el.appendChild(node[0]);
        }else if(load_el.length&&!binding.value)
        {
            load_el[0].remove();
        }
 }
 Vue.directive('loading', {
    bind:loadingHandle,
    update:loadingHandle 
})
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function programmatic_demo()
{

    demo_contextMenu('svg g[data-id="4"]');
    demo_clickOnNode('svg g[data-id="4"]');
}


function demo_contextMenu(jquerySelector)
{

    var jqueryHoverItem = $(jquerySelector)
    var divElement = jqueryHoverItem[0];
    var simulateDivClick = document.createEvent('MouseEvents');
    
    var posItem = $(jquerySelector+' circle')
    
    if (typeof posItem == 'undefined' ||typeof posItem.offset() == 'undefined' )
    {
        console.log("couldn't find anything in contextMenu");
        return;
    }
    var posx = posItem.offset().left+3;
    var posy = posItem.offset().top+3;
    simulateDivClick.initMouseEvent(
            'contextmenu',
            true,
            false,
            window,
            1,
            posx,
            posy,
            posx,
            posy,
            false,
            false,
            false,
            0,
            null,
            null);
    divElement.dispatchEvent(simulateDivClick);        
}



function demo_mouseover()
{
    console.log("mouseover")
    /*
     var evObj = document.createEvent('MouseEvents');
     
     evObj.initMouseEvent(
     "mouseover", //event type : click, mousedown, mouseup, mouseover, mousemove, mouseout.  
     true, //canBubble
     false, //cancelable
     window, //event's AbstractView : should be window 
     1, // detail : Event's mouse click count 
     posx, // screenX
     posy, // screenY
     posx, // clientX
     posy, // clientY
     false, // ctrlKey
     false, // altKey
     false, // shiftKey
     false, // metaKey 
     0, // button : 0 = click, 1 = middle button, 2 = right button  
     null // relatedTarget : Only used with some event types (e.g. mouseover and mouseout). In other cases, pass null.
     );
     elem.dispatchEvent(evObj);
     */
}


function demo_clickOnNode(d3Selector)
{

   // console.log("clickOnNode")
    
    /*
     d3.selectAll('svg')
            .each(
                    function (d, i)
                    {
                        var f = d3.select(this).on("mousedown");
                        console.log("clicking on clear")
                       // f.apply(this, [d], i);
                       // this will clear the right context menus
                       // if its left here the menu pops on and off too fast
                    });
*/
 
    
    
    
    d3.selectAll(d3Selector)
            .each(
                    function (d, i)
                    {
                        var f = d3.select(this).on("click");
                        console.log("clicking on 1")
                        f.apply(this, [d], i);
                    });

}
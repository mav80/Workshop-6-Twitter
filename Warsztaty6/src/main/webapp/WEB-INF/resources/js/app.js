
$(document).ready(function(){
	
	//console.log("JS żyje!");
	
    if ($(".UserIsLogged").length > 0) {
    	console.log('Użytkownik jest zalogowany.');
    } else {
    	console.log('Brak zalogowanego użytkownika.');
    }
    
	

    var commentsLists = $('.commentListList');
    console.log(commentsLists.length);


    for(var i = 0; i < commentsLists.length; i++) {

        var commentEntries = $(commentsLists[i]).find('li');
        console.log(commentEntries); 

        for(var x = 0; x < commentEntries.length - 3; x++) {
            $(commentEntries[x]).parent().parent().parent().hide();

        }

        //add button to show all comments where number of comments is > 3
        if(commentEntries.length > 3) {
            $(commentsLists[i]).children().last().append($('<input type="submit" class="showAllCommentsButton" value="Pokaż wszystkie komentarze">'));
        }

        //add button to show form for adding a new comment to tweet
        
        if ($(".UserIsLogged").length > 0) {
        	$(commentsLists[i]).children().last().append($('<input type="submit" class="addNewCommentButton" value="Dodaj nowy">'));
        } 
        
       

    }


    //reveal all hidden comments and remove button
    var showAllCommentsButtons = $('.showAllCommentsButton');
    console.log(showAllCommentsButtons);
    
    showAllCommentsButtons.on("click", function(e) {

        $(this).parent().parent().find('li').parent().parent().parent().fadeIn();
        this.remove();


    });


    var commentForms = $('.commentForm');
    commentForms.hide();
    
    var showCommentFormButtons = $('.addNewCommentButton');
    showCommentFormButtons.on("click", function(e) {

        $(this).parent().parent().next('.commentForm').fadeIn();
        this.remove();


    });
    
    
    
    //char counter for tweet form
    
    var area = $('.tweetTextArea');
    console.log(area);
    var tweetCharCounter = $(area).siblings('.tweetCharCounter');
    console.log(tweetCharCounter);
    
    area.on("input", function () {
    	
    	tweetCharCounter.text('Stwórz nowego tweeta. Pozostało ' + (280 - area.val().length) + ' znaków do wpisania:');
 
    	var text = area.val().substring(0, 279);
    	area.val(text);
    
    });
    
    
    
    //char counter for comment forms
    
    var commentArea = $('.commentTextArea');
    //console.log(commentArea);

    commentArea.each(function() {
    	
    	$(this).on("input", function () {
    		
    	    var tweetCharCounter = $(this).siblings('.commentCharCounter');
    	    //console.log(tweetCharCounter);
        	
        	tweetCharCounter.text('Napisz nowy komentarz. Pozostało ' + (60 - $(this).val().length) + ' znaków do wpisania:');
     
        	var text = $(this).val().substring(0, 59);
        	$(this).val(text);
    	
    	});
    
    });
    
    
    //char counter for message form
    
    //topic field
    var messageTopicArea = $('.messageTopicArea');
    console.log(messageTopicArea);
    
    messageTopicArea.on("input", function () {
    	
    	var text = messageTopicArea.val().substring(0, 29);
    	messageTopicArea.val(text);
    
    });
    
    //text field
    var messageArea = $('.messageTextArea');
    console.log(messageArea);
    var messageCharCounter = $(messageArea).siblings('.messageCharCounter');
    console.log(tweetCharCounter);
    
    messageArea.on("input", function () {
    	
    	messageCharCounter.text('Pozostało ' + (2048 - messageArea.val().length) + ' znaków do wpisania:');
 
    	var text = messageArea.val().substring(0, 2047);
    	messageArea.val(text);
    
    });
    
    
    
    
    
    // here we ask user for confirmation before deleting something
	var confirms = document.getElementsByClassName('confirm');
	console.log(confirms);
	
	var confirmIt = function (e) {
	    if (!confirm('Jesteś pewien?')) e.preventDefault();
	};
	for (var i = 0, l = confirms.length; i < l; i++) {
		confirms[i].addEventListener('click', confirmIt, false);
	}
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    




});




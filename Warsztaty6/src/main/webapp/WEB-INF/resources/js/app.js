
$(document).ready(function(){
	
	//console.log("JS żyje!");
	

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
        $(commentsLists[i]).children().last().append($('<input type="submit" class="addNewCommentButton" value="Dodaj nowy">'));

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
    	
    	tweetCharCounter.text('Stwórz nową wiadomość. Pozostało ' + (280 - area.val().length) + ' znaków do wpisania:');
 
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
    
    
    



});





$(document).ready(function(){
	
	//console.log("JS żyje!");
	

    var commentsLists = $('.commentListList');
    console.log(commentsLists.length);


    for(var i = 0; i < commentsLists.length; i++) {

        var commentEntries = $(commentsLists[i]).find('li');
        console.log(commentEntries); 

        for(var x = 0; x < commentEntries.length - 3; x++) {
            $(commentEntries[x]).hide();

        }

        //add button to show all comments where number of comments is > 3
        if(commentEntries.length > 3) {
            $(commentsLists[i]).children().last().append($('<input type="submit" class="showAllCommentsButton" value="Pokaż wszystkie komentarze">'));
        }

        //add button to show form for adding a new comment to tweet
        $(commentsLists[i]).children().last().append($('<input type="submit" class="addNewCommentButton" value="Dodaj nowy komentarz">'));

    }


    //reveal all hidden comments and remove button
    var showAllCommentsButtons = $('.showAllCommentsButton');
    console.log(showAllCommentsButtons);
    
    showAllCommentsButtons.on("click", function(e) {

        $(this).parent().parent().find('li').fadeIn();
        this.remove();


    });


    var commentForms = $('.commentForm');

    commentForms.hide();
    
    var showCommentFormButtons = $('.addNewCommentButton');
    
    showCommentFormButtons.on("click", function(e) {

       $(this).parent().parent().parent().find('form').fadeIn();
        this.remove();


    });
    
    
    
    



});




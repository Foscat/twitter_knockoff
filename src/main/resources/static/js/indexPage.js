console.log("Connected");

$(document).ready(() =>{
    $(".unfollowBtn").hover(() => {
        $(this).removeClass("btn-primary");
        $(this).addClass("btn-danger");
        $(this).html("Unfollow");
    }, () => {
        $(this).html("Following");
        $(this).removeClass("btn-danger");
        $(this).addClass("btn-primary");
    });
});
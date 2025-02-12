$(document).ready(function() {
  let $signup = $(".links li #signup"); 
  let $signin = $(".links li #signin");
  let $reset = $(".links li #reset"); 
  let $firstInput = $("form").find(".first-input");
  let $hiddenInput = $("form").find(".input__block #repeat__password");
  let $signinBtn = $("form").find(".signin__btn");

  //----------- sign up ---------------------
  $signup.on("click", function(e) {
    e.preventDefault();
    $(this).parent().parent().siblings("h1").text("SIGN UP");
    $(this).parent().css("opacity", "1");
    $(this).parent().siblings().css("opacity", ".6");
    $firstInput.removeClass("first-input__block").addClass("signup-input__block");
    $hiddenInput.stop(true, true).fadeIn(200);  // 애니메이션 효과 추가
    $signinBtn.text("Sign up");
  });

  //----------- sign in ---------------------
  $signin.on("click", function(e) {
    e.preventDefault();
    $(this).parent().parent().siblings("h1").text("SIGN IN");
    $(this).parent().css("opacity", "1");
    $(this).parent().siblings().css("opacity", ".6");
    $firstInput.addClass("first-input__block")
      .removeClass("signup-input__block");
    $hiddenInput.stop(true, true).fadeOut(200);  // 애니메이션 효과 추가
    $signinBtn.text("Sign in");
  });

  //----------- reset ---------------------
  $reset.on("click", function(e) {
    e.preventDefault();
    $(this).parent().parent().siblings("form")
    .find(".input__block .input").val("");  // 입력 필드 값 초기화
  });
});

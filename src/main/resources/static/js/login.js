$(document).ready(function() {
  let $signup = $(".links li #signup"); 
  let $signin = $(".links li #signin"); 
  let $firstInput = $("form").find(".first-input");
  let $hiddenInput = $("form").find(".input__block #repeat__password");
  let $signinBtn = $("form").find(".signin__btn");
  let $triangle = $(".triangle"); // 삼각형 요소 추가

  //----------- sign up ---------------------
  $signup.on("click", function(e) {
    e.preventDefault();
    $(this).parent().parent().siblings("h1").text("SIGN UP");
    $(this).parent().css("opacity", "1");
    $(this).parent().siblings().css("opacity", ".6");
    $firstInput.removeClass("first-input__block").addClass("signup-input__block");
    $hiddenInput.stop(true, true).fadeIn(200);  // 애니메이션 효과 추가
    $signinBtn.text("Sign up");
    $triangle.css("left", "150px"); // 삼각형 위치 변경
    $(".links li a").css("margin-left", "30mm"); // 버튼을 삼각형 위치에 맞춰 이동
  });

  //----------- sign in ---------------------
  $signin.on("click", function(e) {
    e.preventDefault();
    $(this).parent().parent().siblings("h1").text("SIGN IN");
    $(this).parent().css("opacity", "1");
    $(this).parent().siblings().css("opacity", ".6");
    $firstInput.addClass("first-input__block").removeClass("signup-input__block");
    $hiddenInput.stop(true, true).fadeOut(200);  // 애니메이션 효과 추가
    $signinBtn.text("Sign in");
    $triangle.css("left", "50px"); // 삼각형 위치 변경
    $(".links li a").css("margin-left", "20mm"); // 버튼을 삼각형 위치에 맞춰 이동
  });

  // OR 구분선 복원
  $(".separator").html("<p>OR</p>");
});

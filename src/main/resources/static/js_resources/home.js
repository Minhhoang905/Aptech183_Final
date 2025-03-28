
//Hiệu ứng khiến chữ Complex xuất hiện và mở rộng ra
gsap.from("#sec_1 h2",{
    color:"transparent",
    fontSize:0,
    // opacity:"100%",
    duration: 2,
    delay:0.7,
})

//Hiệu ứng cho The - Gym hiện ra
gsap.from("#sec_1 h3",{
    y: 100,
    color:"transparent",
    duration:2,
    delay:0.5
})

// Hiệu ứng chuyển toàn bộ background color sang black
gsap.to("section",{
    backgroundColor:"black",
    duration:2,
    scrollTrigger:{
        trigger:("#sec_1"),
        start:("45% center"),
        scrub: 0.7,
    }
})

let tl1 = gsap.timeline({
    scrollTrigger:{
        trigger:("#sec_2"),
        start: ("center center"),
        end: ("+=700"),
        pin:true,
        pinSpacing:false,
        scrub:1
    }
})

tl1.to("#sec_2 .content_left",{
    transform: ("scaleX(1)"),
    duration:1,
    stagger:1
}).to("#sec_2 .content_right",{
    duration:1,
    transform: ("scaleX(1)")
}).to("#sec_2 .content_center",{
    duration:1,
    transform: ("scaleX(1)")
}).to("#sec_2",{
    duration:3,
    delay:4
})


let tl2 = gsap.timeline({
    scrollTrigger:{
        trigger:("#sec_3"),
        start:("center center"),
        pin: true,
        pinSpacing: false,
        scrub: 1
    }
})

//Tạo split text cho sec3 h3
// let sec3Split = new SplitText("#text",{type:"words, chars"});
// let sec3Char = sec3Split.chars;

tl2.from("#sec_3 .sec_3_img",{
    borderRadius: 1000,
    duration: 2,
    delay: 1,
    scale:0
}).from("#sec_3 h2",{
    opacity: 0,
    duration: 2,
    delay: 2,
    stagger: 1
}).from("#sec_3 h3",{
    opacity: 0,
    duration: 2,
    delay: 2,
    stagger: 1
})

//Timeline cho section 4: intro
let tl3 = gsap.timeline({
    scrollTrigger:{
        trigger:("#sec_4 .sec_4_intro"),
        start:("center center"),
        pin: true,
        pinSpacing: false,
        scrub: 2
    }
})

tl3.from("#sec_4 .sec_4_intro h3",{
    opacity: 0,
    duration: 2,
    delay: 1,
    stagger: 1
}).from("#sec_4 .sec_4_intro h2",{
    opacity: 0,
    duration: 2,
    delay: 1,
    stagger:1
})

//Timeline cho section 4: content
let tl4 = gsap.timeline({
    scrollTrigger:{
        trigger:("#sec_4 .sec_4_content"),
        start:("center center"),
        pin: true,
        scrub: 2,
        pinSpacing: false
    }
})

tl4.from("#sec_4 .sec_4_content .sec_4_text .text_1",{
    x: -1000,
    duration: 2,
    delay:1
}).from("#sec_4 .sec_4_content .sec_4_text .text_2",{
    x: -1500,
    duration: 2,
    delay:1
}).from("#sec_4 .sec_4_content .sec_4_text .text_3",{
    x: -2000,
    duration: 2,
    delay:1
}).to("#sec_4 .sec_4_content",{
    duration: 5,

})



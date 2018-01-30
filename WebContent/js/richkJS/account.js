function serUserNearBrand() {

    $(document).ready(function () {
        $.get("user", /*data,*/ function (data, status) {

            user = data[0];
            document.getElementById("userNearBrand").innerHTML = " - " + user.email + " (ADMIN)";

        }, "json"/*, dataType*/)
            .done(function () {
                //alert("second success");
            })
            .fail(function () {
                //alert( "error" );
            })
            .always(function () {
                //alert("finished");
            });
    });

}
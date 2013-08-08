    var j4p = new Jolokia({url: "/jolokia", fetchInterval: 1000});

    var context = cubism.context()
        .serverDelay(0)
        .clientDelay(0)
        .step(1000)
        .size(594);
    
    var jolokia = context.jolokia(j4p);

    // =============================================================================
    // Métricas

    var memory = jolokia.metric(
        function (resp1, resp2) {
            return Number(resp1.value) / Number(resp2.value);
        },
        {type:"read", mbean:"java.lang:type=Memory", attribute:"HeapMemoryUsage", path:"used"},
        {type:"read", mbean:"java.lang:type=Memory", attribute:"HeapMemoryUsage", path:"max"}, "Heap-Memory"
    );
    
    var harvesters =  jolokia.metric(
       function (resp1) {
            return resp1.value;
        },
        {type:"read", mbean:"backend:name=snapshotManager", attribute:"ActiveSnapshotsCount"}, "Cosechas Activas"
    );
    
    var colorsRed = ["#FDBE85", "#FEEDDE", "#FD8D3C", "#E6550D", "#A63603", "#FDBE85", "#FEEDDE", "#FD8D3C", "#E6550D", "#A63603" ],
        colorsGreen = [ "#E5F5F9", "#99D8C9", "#2CA25F", "#E5F5F9", "#99D8C9", "#2CA25F"],
        colorsBlue = [ "#ECE7F2", "#A6BDDB", "#2B8CBE", "#ECE7F2", "#A6BDDB", "#2B8CBE"];

    // Created graphs
    $(function () {
    	
    
        d3.select("#memory").call(function (div) {

            div.append("div")
                .attr("class", "axis")
                .call(context.axis().orient("top"));

            div.selectAll(".horizon")
                .data([memory])
                .enter().append("div")
                .attr("class", "horizon")
                .call(
                	context.horizon()
                    .colors(colorsRed)
                    .format(d3.format(".4p"))
            );
           
            div.append("div")
                .attr("class", "rule")
                .call(context.rule());
            
        });
        
        d3.select("#harvesters").call(function (div) {

                div.append("div")
                    .attr("class", "axis")
                    .call(context.axis().orient("top"));

                div.selectAll(".horizon")
                    .data([harvesters])
                    .enter().append("div")
                    .attr("class", "horizon")
                    .call(
                    	context.horizon()
                        .colors(colorsBlue)
                        .format(d3.format("d"))
                );
               
                div.append("div")
                    .attr("class", "rule")
                    .call(context.rule());

        });

        // On mousemove, reposition the chart values to match the rule.
        context.on("focus", function (i) {
            d3.selectAll("#memory .value").style("right", i == null ? null : context.size() - i + "px");
        });

    });

    function gc() {
        j4p.request({type:"exec", mbean:"java.lang:type=Memory", operation:"gc"});
    };
		 
    
    j4p.start(1000);

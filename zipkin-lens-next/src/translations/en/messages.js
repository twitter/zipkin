/* eslint-disable */export default{localeData:{"plurals":function(n,ord){var s=String(n).split("."),v0=!s[1],t0=Number(s[0])==n,n10=t0&&s[0].slice(-1),n100=t0&&s[0].slice(-2);if(ord)return n10==1&&n100!=11?"one":n10==2&&n100!=12?"two":n10==3&&n100!=13?"few":"other";return n==1&&v0?"one":"other"}},messages:{"Address":"Address","Annotations":"Annotations","Change Language":"Change Language","Dependencies":"Dependencies","Dependencies Page":"Dependencies Page","Depth":"Depth","Discover":"Discover","Discover Page":"Discover Page","Download JSON":"Download JSON","Duration":"Duration","End Time":"End Time","Failed to load this file":"Failed to load this file","Filter":"Filter","Limit":"Limit","None":"None","Only V2 format is supported":"Only V2 format is supported","Parent ID":"Parent ID","Please select criteria in the search bar. Then, click the search button.":"Please select criteria in the search bar. Then, click the search button.","Please select the criteria for your trace lookup":"Please select the criteria for your trace lookup","Please select the start and end time. Then, click the search button.":"Please select the start and end time. Then, click the search button.","Relative Time":"Relative Time","Repository":"Repository","Root":"Root","Search Dependencies":"Search Dependencies","Search Traces":"Search Traces","Searching has been disabled via the searchEnabled property. You can still view specific traces of which you know the trace id by entering it in the \"trace id...\" textbox on the top-right.":"Searching has been disabled via the searchEnabled property. You can still view specific traces of which you know the trace id by entering it in the \"trace id...\" textbox on the top-right.","Service Name":"Service Name","Services":"Services","Span ID":"Span ID","Start Time":"Start Time","Tags":"Tags","This file does not contain JSON":"This file does not contain JSON","Total Spans":"Total Spans","Trace ID":"Trace ID","Upload JSON":"Upload JSON","View Logs":"View Logs","Zipkin Home":"Zipkin Home","hide annotations":"hide annotations","show all annotations":"show all annotations","trace id...":"trace id...","{0, plural, one {# Result} other {# Results}}":[["0","plural",{one:["#"," Result"],other:["#"," Results"]}]]}};
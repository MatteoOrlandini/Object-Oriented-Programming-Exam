

---

<h1 id="exam">Exam</h1>
<p>The purpose of the code is to visualize the assigned data-set and create a set of classes that represent it.<br>
(The UML representations are in the bottom of this file).<br>
Our data-set describes a list of pharmacies.<br>
The JAVA application has the following features:</p>
<h2 id="section"></h2>
<p><strong>On start-up</strong>: download the data-set (zip or csv) which contains data in CSV format starting from the address provided after decoding the JSON containing the URL useful for downloading the file.  This phase will come performed when the application is first started.</p>
<p>This is done by reading from a external configuration file the url and the name of the data-set destination file.<br>
The JSON contained in the url is then scanned by the class <em>JSONParser</em> that identify the location of the second url, allowing the download the data-set.<br>
There is a file presence check that can prevent to download an existing file.</p>
<h2 id="section-1"></h2>
<p><strong>When the download is completed</strong>: parse the data by creating appropriate data structures based on the above classes (each data-set record corresponds to an object of a class).</p>
<p>The class <em>CSVParser</em> is used to read and parse the CSV file and to fill a vector of <em>Pharmacy</em> and one of <em>Metadata</em>.<br>
Each row represents a <em>Pharmacy</em> and each field of columns represents a <em>Metadata</em>.<br>
A pharmacy is described by the following fields:</p>
<ul>
<li>id (CODICEIDENTIFICATIVOSITO)</li>
<li>name (DENOMINAZIONESITOLOGISTICO)</li>
<li>address (INDIRIZZO)</li>
<li>vatNumber (PARTITAIVA)</li>
<li>postalCode (CAP)</li>
<li>cityCode (CODICECOMUNEISTAT)</li>
<li>city (DESCRIZIONECOMUNE)</li>
<li>provinceCode (CODICEPROVINCIAISTAT)</li>
<li>provinceAbbreviation (SIGLAPROVINCIA)</li>
<li>provinceName (DESCRIZIONEPROVINCIA)</li>
<li>regionCode (CODICEREGIONE)</li>
<li>regionName (DESCRIZIONEREGIONE)</li>
<li>beginValidity (DATAINIZIOVALIDITA)</li>
<li>endValidity (DATAFINEVALIDITA)</li>
<li>latitude (LATITUDINE)</li>
<li>longitude (LONGITUDINE)</li>
<li>localize (LOCALIZE)</li>
</ul>
<p>There are three cases of misspelled fields that needs to be corrected manually.</p>
<pre><code>public String lineCorrection(String str) {
		String str2 = str;
		if (str.contains("\"Via Cappuccini ;163\"")) {
			str2 = str.replace("\"Via Cappuccini ;163\"", "\"Via Cappuccini ,163\"");
		}
		if (str.contains("\"via vespucci;26\"")) {
			str2 = str.replace("\"via vespucci;26\"", "\"via vespucci,26\"");
		}
		if (str.contains("\'9,258444")) {
			str2 = str.replace("\'9,258444", "9.258444");
		}
		return str2;
	}
</code></pre>
<p>We also corrected the values of latitude and longitude, replacing the comma with the point and the not allowed values with “-360”.</p>
<pre><code>public String[] coordinateCorrection(String[] str) {
		String[] str2 = str;
	for (int i = 14; i &lt;= 15; i++) {
		if (str[i].contains(","))
			str2[i] = str[i].replace(',', '.');

		if (str[i].equals("-"))
			str2[i] = str[i].replace("-", "-360");
		if (str[i].equals("0"))
			str2[i] = str[i].replace("0", "-360");
	}
	return str2;
}
</code></pre>
<h2 id="section-2"></h2>
<p><strong>On request</strong>: return statistics and filtered dataset using API REST GET or POST.<br>
Afterwards the various requests that can be carried out with relevant examples will be listed.<br>
The examples refer to the query or to the body of the POST (JSON).<br>
The code quoted is not explanatory of the project but serves to give an idea of ​​the reasoning behind.</p>
<h2 id="get-requests">GET requests:</h2>
<h3 id="data"><strong>/data</strong></h3>
<p>Returns all the data-set.</p>
<h3 id="metadata"><strong>/metadata</strong></h3>
<p>Returns all the fields.</p>
<h3 id="statsfieldname"><strong>/stats/{fieldName}</strong></h3>
<p>Gives statistics on numbers based on the class <em>NumberStats</em>:</p>
<ul>
<li>Average</li>
<li>Minimum</li>
<li>Maximum</li>
<li>Standard Deviation</li>
<li>Sum</li>
</ul>
<p>The fields on which it makes sense to carry out statistics are only latitude and longitude.<br>
All the stats are calculated in a single for cycle:</p>
<pre><code>    int count = store.size();
	double avg = 0;
	double min = store.get(0);
	double max = store.get(0);
	double std = 0;
	double sum = 0;
	for (Double item : store) {
		avg += item;
		if (item &lt; min)
			min = item;
		if (item &gt; max)
			max = item;
		sum += item;
		std += item * item;
	}
	avg = avg / count;
	std = Math.sqrt((count * std - sum * sum) / (count * count));
	NumberStats stats = new NumberStats(avg, min, max, std, sum);
	return stats;
</code></pre>
<p>To do it standard deviation is calulated with:<br>
<span class="katex--display"><span class="katex-display"><span class="katex"><span class="katex-mathml"><math><semantics><mrow><msub><mi>σ</mi><mi>x</mi></msub><mo>=</mo><mfrac><mn>1</mn><mi>N</mi></mfrac><msqrt><mrow><mi>N</mi><munderover><mo>∑</mo><mrow><mi>i</mi><mo>=</mo><mn>0</mn></mrow><mi>N</mi></munderover><msubsup><mi>x</mi><mi>i</mi><mn>2</mn></msubsup><mo>−</mo><mo fence="false">(</mo><munderover><mo>∑</mo><mrow><mi>i</mi><mo>=</mo><mn>0</mn></mrow><mi>N</mi></munderover><msub><mi>x</mi><mi>i</mi></msub><msup><mo fence="false">)</mo><mn>2</mn></msup></mrow></msqrt></mrow><annotation encoding="application/x-tex">
\sigma_x={1\over N}\sqrt{N\sum_{i=0}^N x_i^2-\bigg (\sum_{i=0}^N x_i\bigg) ^2}
</annotation></semantics></math></span><span class="katex-html" aria-hidden="true"><span class="base"><span class="strut" style="height: 0.58056em; vertical-align: -0.15em;"></span><span class="mord"><span class="mord mathit" style="margin-right: 0.03588em;">σ</span><span class="msupsub"><span class="vlist-t vlist-t2"><span class="vlist-r"><span class="vlist" style="height: 0.151392em;"><span class="" style="top: -2.55em; margin-left: -0.03588em; margin-right: 0.05em;"><span class="pstrut" style="height: 2.7em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mathit mtight">x</span></span></span></span><span class="vlist-s">​</span></span><span class="vlist-r"><span class="vlist" style="height: 0.15em;"><span class=""></span></span></span></span></span></span><span class="mspace" style="margin-right: 0.277778em;"></span><span class="mrel">=</span><span class="mspace" style="margin-right: 0.277778em;"></span></span><span class="base"><span class="strut" style="height: 3.33376em; vertical-align: -1.27767em;"></span><span class="mord"><span class="mord"><span class="mopen nulldelimiter"></span><span class="mfrac"><span class="vlist-t vlist-t2"><span class="vlist-r"><span class="vlist" style="height: 1.32144em;"><span class="" style="top: -2.314em;"><span class="pstrut" style="height: 3em;"></span><span class="mord"><span class="mord mathit" style="margin-right: 0.10903em;">N</span></span></span><span class="" style="top: -3.23em;"><span class="pstrut" style="height: 3em;"></span><span class="frac-line" style="border-bottom-width: 0.04em;"></span></span><span class="" style="top: -3.677em;"><span class="pstrut" style="height: 3em;"></span><span class="mord"><span class="mord">1</span></span></span></span><span class="vlist-s">​</span></span><span class="vlist-r"><span class="vlist" style="height: 0.686em;"><span class=""></span></span></span></span></span><span class="mclose nulldelimiter"></span></span></span><span class="mord sqrt"><span class="vlist-t vlist-t2"><span class="vlist-r"><span class="vlist" style="height: 2.05609em;"><span class="svg-align" style="top: -5.29375em;"><span class="pstrut" style="height: 5.29375em;"></span><span class="mord" style="padding-left: 1.056em;"><span class="mord mathit" style="margin-right: 0.10903em;">N</span><span class="mspace" style="margin-right: 0.166667em;"></span><span class="mop op-limits"><span class="vlist-t vlist-t2"><span class="vlist-r"><span class="vlist" style="height: 1.82834em;"><span class="" style="top: -1.87233em; margin-left: 0em;"><span class="pstrut" style="height: 3.05em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mtight"><span class="mord mathit mtight">i</span><span class="mrel mtight">=</span><span class="mord mtight">0</span></span></span></span><span class="" style="top: -3.05001em;"><span class="pstrut" style="height: 3.05em;"></span><span class=""><span class="mop op-symbol large-op">∑</span></span></span><span class="" style="top: -4.30001em; margin-left: 0em;"><span class="pstrut" style="height: 3.05em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mathit mtight" style="margin-right: 0.10903em;">N</span></span></span></span><span class="vlist-s">​</span></span><span class="vlist-r"><span class="vlist" style="height: 1.27767em;"><span class=""></span></span></span></span></span><span class="mspace" style="margin-right: 0.166667em;"></span><span class="mord"><span class="mord mathit">x</span><span class="msupsub"><span class="vlist-t vlist-t2"><span class="vlist-r"><span class="vlist" style="height: 0.795908em;"><span class="" style="top: -2.42314em; margin-left: 0em; margin-right: 0.05em;"><span class="pstrut" style="height: 2.7em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mathit mtight">i</span></span></span><span class="" style="top: -3.0448em; margin-right: 0.05em;"><span class="pstrut" style="height: 2.7em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mtight">2</span></span></span></span><span class="vlist-s">​</span></span><span class="vlist-r"><span class="vlist" style="height: 0.276864em;"><span class=""></span></span></span></span></span></span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mbin">−</span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mord"><span class="delimsizing size3">(</span></span><span class="mspace" style="margin-right: 0.166667em;"></span><span class="mop op-limits"><span class="vlist-t vlist-t2"><span class="vlist-r"><span class="vlist" style="height: 1.82834em;"><span class="" style="top: -1.87233em; margin-left: 0em;"><span class="pstrut" style="height: 3.05em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mtight"><span class="mord mathit mtight">i</span><span class="mrel mtight">=</span><span class="mord mtight">0</span></span></span></span><span class="" style="top: -3.05001em;"><span class="pstrut" style="height: 3.05em;"></span><span class=""><span class="mop op-symbol large-op">∑</span></span></span><span class="" style="top: -4.30001em; margin-left: 0em;"><span class="pstrut" style="height: 3.05em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mathit mtight" style="margin-right: 0.10903em;">N</span></span></span></span><span class="vlist-s">​</span></span><span class="vlist-r"><span class="vlist" style="height: 1.27767em;"><span class=""></span></span></span></span></span><span class="mspace" style="margin-right: 0.166667em;"></span><span class="mord"><span class="mord mathit">x</span><span class="msupsub"><span class="vlist-t vlist-t2"><span class="vlist-r"><span class="vlist" style="height: 0.311664em;"><span class="" style="top: -2.55em; margin-left: 0em; margin-right: 0.05em;"><span class="pstrut" style="height: 2.7em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mathit mtight">i</span></span></span></span><span class="vlist-s">​</span></span><span class="vlist-r"><span class="vlist" style="height: 0.15em;"><span class=""></span></span></span></span></span></span><span class="mord"><span class="mord"><span class="delimsizing size3">)</span></span><span class="msupsub"><span class="vlist-t"><span class="vlist-r"><span class="vlist" style="height: 1.65401em;"><span class="" style="top: -3.9029em; margin-right: 0.05em;"><span class="pstrut" style="height: 2.7em;"></span><span class="sizing reset-size6 size3 mtight"><span class="mord mtight">2</span></span></span></span></span></span></span></span></span></span><span class="" style="top: -4.01609em;"><span class="pstrut" style="height: 5.29375em;"></span><span class="hide-tail" style="min-width: 0.742em; height: 3.37376em;"><svg width="400em" height="3.373755em" viewBox="0 0 400000 3373" preserveAspectRatio="xMinYMin slice"><path d="M702 80H400000v40H742v3239l-4 4-4 4c-.667.7
-2 1.5-4 2.5s-4.167 1.833-6.5 2.5-5.5 1-9.5 1h-12l-28-84c-16.667-52-96.667
-294.333-240-727l-212 -643 -85 170c-4-3.333-8.333-7.667-13 -13l-13-13l77-155
 77-156c66 199.333 139 419.667 219 661 l218 661zM702 80H400000v40H742z"></path></svg></span></span></span><span class="vlist-s">​</span></span><span class="vlist-r"><span class="vlist" style="height: 1.27767em;"><span class=""></span></span></span></span></span></span></span></span></span></span></p>
<p><em>example:</em></p>
<blockquote>
<p>localhost:8080/stats/longitude</p>
</blockquote>
<p><em>response:</em></p>
<blockquote>
<p>{<br>
“avg”: 10.536053514294705,<br>
“min”: 6.731547284806,<br>
“max”: 18.492467,<br>
“std”: 27.527718697084676,<br>
“sum”: 110533.73741846575 }</p>
</blockquote>
<h3 id="countfieldname"><strong>/count/{fieldName}</strong></h3>
<p>Returns the number of times the string of the specified field occourred.<br>
It needs a field name in the query path and a value to confront in the query params.</p>
<pre><code>(@PathVariable String fieldName, @RequestParam(value = "value") String value)
</code></pre>
<p><em>example:</em></p>
<blockquote>
<p>localhost:8080/count/beginValidity?value=01/10/2006</p>
</blockquote>
<p><em>response</em></p>
<blockquote>
<p>count : 9</p>
</blockquote>
<h2 id="post-requests">POST requests:</h2>
<h3 id="localize"><strong>/localize</strong></h3>
<p>Using a POST request it looks in the body for a latitude, a longitude and a range (in Km) and returns those that are in the specified area.</p>
<p>The formula used to calculate the distance is:</p>
<p><span class="katex--inline"><span class="katex"><span class="katex-mathml"><math><semantics><mrow><mi>p</mi><mn>1</mn><mo>=</mo><mo>(</mo><mi>l</mi><mi>o</mi><mi>n</mi><mn>1</mn><mo separator="true">,</mo><mi>l</mi><mi>a</mi><mi>t</mi><mn>1</mn><mo>)</mo></mrow><annotation encoding="application/x-tex">p1 = (lon1, lat1)</annotation></semantics></math></span><span class="katex-html" aria-hidden="true"><span class="base"><span class="strut" style="height: 0.83888em; vertical-align: -0.19444em;"></span><span class="mord mathit">p</span><span class="mord">1</span><span class="mspace" style="margin-right: 0.277778em;"></span><span class="mrel">=</span><span class="mspace" style="margin-right: 0.277778em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mopen">(</span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">o</span><span class="mord mathit">n</span><span class="mord">1</span><span class="mpunct">,</span><span class="mspace" style="margin-right: 0.166667em;"></span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">a</span><span class="mord mathit">t</span><span class="mord">1</span><span class="mclose">)</span></span></span></span></span> longitude and latitude in radians.<br>
<span class="katex--inline"><span class="katex"><span class="katex-mathml"><math><semantics><mrow><mi>p</mi><mn>2</mn><mo>=</mo><mo>(</mo><mi>l</mi><mi>o</mi><mi>n</mi><mn>2</mn><mo separator="true">,</mo><mi>l</mi><mi>a</mi><mi>t</mi><mn>2</mn><mo>)</mo></mrow><annotation encoding="application/x-tex">p2 = (lon2, lat2)</annotation></semantics></math></span><span class="katex-html" aria-hidden="true"><span class="base"><span class="strut" style="height: 0.83888em; vertical-align: -0.19444em;"></span><span class="mord mathit">p</span><span class="mord">2</span><span class="mspace" style="margin-right: 0.277778em;"></span><span class="mrel">=</span><span class="mspace" style="margin-right: 0.277778em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mopen">(</span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">o</span><span class="mord mathit">n</span><span class="mord">2</span><span class="mpunct">,</span><span class="mspace" style="margin-right: 0.166667em;"></span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">a</span><span class="mord mathit">t</span><span class="mord">2</span><span class="mclose">)</span></span></span></span></span> longitude and latitude in radians.</p>
<p><span class="katex--inline"><span class="katex"><span class="katex-mathml"><math><semantics><mrow><mi>d</mi><mi>i</mi><mi>s</mi><mi>t</mi><mo>=</mo><mi>a</mi><mi>r</mi><mi>c</mi><mi>c</mi><mi>o</mi><mi>s</mi><mo>(</mo><mi>s</mi><mi>i</mi><mi>n</mi><mo>(</mo><mi>l</mi><mi>a</mi><mi>t</mi><mn>1</mn><mo>)</mo><mo>∗</mo><mi>s</mi><mi>i</mi><mi>n</mi><mo>(</mo><mi>l</mi><mi>a</mi><mi>t</mi><mn>2</mn><mo>)</mo><mo>+</mo><mi>c</mi><mi>o</mi><mi>s</mi><mo>(</mo><mi>l</mi><mi>a</mi><mi>t</mi><mn>1</mn><mo>)</mo><mo>∗</mo><mi>c</mi><mi>o</mi><mi>s</mi><mo>(</mo><mi>l</mi><mi>a</mi><mi>t</mi><mn>2</mn><mo>)</mo><mo>∗</mo><mi>c</mi><mi>o</mi><mi>s</mi><mo>(</mo><mi>l</mi><mi>o</mi><mi>n</mi><mn>2</mn><mo>−</mo><mi>l</mi><mi>o</mi><mi>n</mi><mn>1</mn><mo>)</mo><mo>)</mo><mo>∗</mo><mn>6371</mn></mrow><annotation encoding="application/x-tex">dist = arccos( sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2-lon1) ) * 6371</annotation></semantics></math></span><span class="katex-html" aria-hidden="true"><span class="base"><span class="strut" style="height: 0.69444em; vertical-align: 0em;"></span><span class="mord mathit">d</span><span class="mord mathit">i</span><span class="mord mathit">s</span><span class="mord mathit">t</span><span class="mspace" style="margin-right: 0.277778em;"></span><span class="mrel">=</span><span class="mspace" style="margin-right: 0.277778em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mord mathit">a</span><span class="mord mathit" style="margin-right: 0.02778em;">r</span><span class="mord mathit">c</span><span class="mord mathit">c</span><span class="mord mathit">o</span><span class="mord mathit">s</span><span class="mopen">(</span><span class="mord mathit">s</span><span class="mord mathit">i</span><span class="mord mathit">n</span><span class="mopen">(</span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">a</span><span class="mord mathit">t</span><span class="mord">1</span><span class="mclose">)</span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mbin">∗</span><span class="mspace" style="margin-right: 0.222222em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mord mathit">s</span><span class="mord mathit">i</span><span class="mord mathit">n</span><span class="mopen">(</span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">a</span><span class="mord mathit">t</span><span class="mord">2</span><span class="mclose">)</span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mbin">+</span><span class="mspace" style="margin-right: 0.222222em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mord mathit">c</span><span class="mord mathit">o</span><span class="mord mathit">s</span><span class="mopen">(</span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">a</span><span class="mord mathit">t</span><span class="mord">1</span><span class="mclose">)</span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mbin">∗</span><span class="mspace" style="margin-right: 0.222222em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mord mathit">c</span><span class="mord mathit">o</span><span class="mord mathit">s</span><span class="mopen">(</span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">a</span><span class="mord mathit">t</span><span class="mord">2</span><span class="mclose">)</span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mbin">∗</span><span class="mspace" style="margin-right: 0.222222em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mord mathit">c</span><span class="mord mathit">o</span><span class="mord mathit">s</span><span class="mopen">(</span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">o</span><span class="mord mathit">n</span><span class="mord">2</span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mbin">−</span><span class="mspace" style="margin-right: 0.222222em;"></span></span><span class="base"><span class="strut" style="height: 1em; vertical-align: -0.25em;"></span><span class="mord mathit" style="margin-right: 0.01968em;">l</span><span class="mord mathit">o</span><span class="mord mathit">n</span><span class="mord">1</span><span class="mclose">)</span><span class="mclose">)</span><span class="mspace" style="margin-right: 0.222222em;"></span><span class="mbin">∗</span><span class="mspace" style="margin-right: 0.222222em;"></span></span><span class="base"><span class="strut" style="height: 0.64444em; vertical-align: 0em;"></span><span class="mord">6</span><span class="mord">3</span><span class="mord">7</span><span class="mord">1</span></span></span></span></span><br>
(6371 in the Earth radius in Km; dist is expressed in Km).</p>
<p><em>example:</em></p>
<blockquote>
<p>{<br>
“latitude”: 41.55,<br>
“longitude”: 15.22,<br>
“range”: 10<br>
}</p>
</blockquote>
<h3 id="filter"><strong>/filter</strong></h3>
<p>Generic filter using a POST. If the body of the JSON is a single object it searches for a field, an operator and an input value and returns the filtered dataset. If it is found an attribute called “$or” or “$and” it applies multiple filters to the following array of object based on the attribute. The “$or” filter does a filter for each object and then unites them. The “$and” filter just recursively filter the result of the previous decimation.</p>
<p>or:</p>
<pre><code>for (Object obj : jsonArray) {
				filterParam.readFields(obj);
				tempOr = pharmacyService.filter(pharmacyService.getPharmacies(), filterParam);
				for (Pharmacy item : tempOr) {
					if (!temp.contains(item))
						temp.add(item);
				} 		
	}
</code></pre>
<p>and:</p>
<pre><code>for (Object obj : jsonArray) {
					filterParam.readFields(obj);
					// iteration on .filter
					temp = pharmacyService.filter(temp, filterParam);
				}
</code></pre>
<p><em>examples:</em></p>
<ol>
<li>
<blockquote>
<p>{<br>
“fieldName”: “city”,<br>
“operator”: “==”,<br>
“value”: “Ancona”<br>
}</p>
</blockquote>
</li>
<li>
<blockquote>
<p>{<br>
“$or”: [<br>
{<br>
“fieldName”: “latitude”,<br>
“operator”: “&lt;=”,<br>
“value”: “43.1”<br>
},<br>
{<br>
“fieldName”: “provinceName”,<br>
“operator”: “==”,<br>
“value”: “Ancona”<br>
}<br>
]<br>
}</p>
</blockquote>
</li>
<li>
<blockquote>
<p>{<br>
“$and”: [<br>
{<br>
“fieldName”: “postalCode”,<br>
“operator”: “==”,<br>
“value”: “28021”<br>
},<br>
{<br>
“fieldName”: “beginValidity”,<br>
“operator”: “&lt;”,<br>
“value”: “02/05/2015”<br>
}<br>
]<br>
}</p>
</blockquote>
</li>
<li>
<blockquote>
<p>{<br>
“$and”: [<br>
{<br>
“fieldName”: “regionName”,<br>
“operator”: “==”,<br>
“value”: “Puglia”<br>
},<br>
{<br>
“fieldName”: “endValidity”,<br>
“operator”: “&gt;”,<br>
“value”: “20/04/2012”<br>
},<br>
{<br>
“fieldName”: “endValidity”,<br>
“operator”: “&lt;”,<br>
“value”: “20/05/2012”<br>
}<br>
]<br>
}</p>
</blockquote>
</li>
</ol>
<h3 id="filterstatsfieldname"><strong>/filter/stats/{fieldName}</strong></h3>
<p>Gives stats like the request “/stats/{fieldName}” but on a sample made of the filtrated pharmacies.<br>
In the body we put the specifics of the filter like with “/filter” and in the query there must be latitude or longitude.</p>
<p><em>example:</em></p>
<blockquote>
<p>localhost:8080/filter/stats/latitude</p>
</blockquote>
<blockquote>
<p>{<br>
“$and”: [<br>
{<br>
“fieldName”: “city”,<br>
“operator”: “==”,<br>
“value”: “Bari”<br>
},<br>
{<br>
“fieldName”: “beginValidity”,<br>
“operator”: “&gt;”,<br>
“value”: “01/01/2012”<br>
},<br>
{<br>
“fieldName”: “beginValidity”,<br>
“operator”: “&lt;”,<br>
“value”: “31/12/2012”<br>
}<br>
]<br>
}</p>
</blockquote>
<p><em>response:</em></p>
<blockquote>
<p>{<br>
“avg”: 41.09908327509544,<br>
“min”: 41.0453375512566,<br>
“max”: 41.1256631,<br>
“std”: 0.02907812314007932,<br>
“sum”: 287.69358292566807 }</p>
</blockquote>
<h2 id="section-3"></h2>
<p><a href="https://drive.google.com/open?id=1mm4TOyTvOkuXTh9-gx_YGpTbR7DFwZFr">UML CLass Diagram</a></p>
<p><a href="https://drive.google.com/open?id=14CkKrcjyybDNvNPgcg8gnntwBIUatB8H">UML Use Case Diagram</a></p>
<p><a href="http://drive-html-viewer.pansy.at/?state=%7B%22ids%22:%5B%221Q1Y26NUKOnGE4RGMDD6t9_eywuExcJLn%22%5D,%22action%22:%22open%22,%22userId%22:%22117028957555747698312%22%7D">UML Sequence Diagram</a></p>

<!--stackedit_data:
eyJoaXN0b3J5IjpbMTk2MjI0MjExLDE3MDYyNDg5MTJdfQ==
-->
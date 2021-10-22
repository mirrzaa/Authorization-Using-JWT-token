# Authorization-Using-JWT-token
![first_image](/images/1.png)



<h1>To accomplish our task at a high level, we need to:</h1>
<p>1. Create endpoint that represents the resource we want to secure.</p>
<p>2. Implement the first authentication step in which client side sends user credentials(username, password) to business logic server to login.</p>
<p>3. Implement the second authentication step in which client side sends the OTP the user receives from authentication server to the business logic server.</p>
<p>Once authenticated by the OTP, the client gets back JWT token, which can access a user's resource.</p>
<p>4. Implement authorization based on the JWT. The business logic server validates the JWT received from client, and if valid allows the client to access the resource.</p>

<h1>Technically to achieve these four high-level end points we need to:</h1>
<p>1. Create the business logic server.</p>
<p>2. Implement the Authentication objects that have the role of representing authentication steps.</p>
<p>3. Impelement a proxy to establish communication betweeen authentication server and business logic server.</p>
<p>4. Define the AuthenticationProvider objects that implement athentication logic for two authentication steps using Authentication objects defined in step2. </p>
<p>5. Define Custom filter objects that intercept the HTTP request and apply authentication logic implemented by the AuthenticationProvider objects.</p>
<p>6. Write Authorization configurations.</p>

![first_image](/images/3.png)
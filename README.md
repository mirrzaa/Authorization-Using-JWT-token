# Authorization-Using-JWT-token

![first_image](/images/1.png)

<h1>To accomplish our task at a high level, we need to:</h1>
<table>
<li>Step 1: Create endpoint that represents the resource we want to secure.</li>
<li>Step 2: Implement the first authentication step in which client side sends user credentials(username, password) to business logic server to login.</li>
<li>Step 3: Implement the second authentication step in which client side sends the OTP the user receives from authentication server to the business logic server.</li>
Once authenticated by the OTP, the client gets back JWT token, which can access a user's resource.
<li>Step 4: Implement authorization based on the JWT. The business logic server validates the JWT received from client, and if valid allows the client to access the resource.</li>
</table>
<h1>Technically to achieve these four high-level end points we need to:</h1>
<li>Step 1: Create the business logic server.</li>
<li>Step 2: Implement the Authentication objects that have the role of representing authentication steps.</li>
<li>Step 3: Impelement a proxy to establish communication betweeen authentication server and business logic server.</li>
<li>Step 4: Define the AuthenticationProvider objects that implement athentication logic for two authentication steps using Authentication objects defined in step2. </li>
<li>Step 5: Define Custom filter objects that intercept the HTTP request and apply authentication logic implemented by the AuthenticationProvider objects.</li>
<li>Step 6: Write Authorization configurations.</li>

![first_image](/images/3.png)

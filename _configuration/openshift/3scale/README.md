Dynamic 3scale Developer Portal signup templates
================================================
Article: https://developers.redhat.com/blog/2017/12/18/3scale-developer-portal-signup-flows/#targetText=3scale%20creates%20an%20Application%20in,successfully%20consume%20their%20subscribed%20Services.

There are 4 custom signup flows included in the [parent homepage](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/index.html.liquid). These flows are included into the homepage using Liquid tags such as `{% include 'partial name' %}` because the flows are separated out into individual partials. The partials can be included in your 3scale portal individually or all together depending on which flows you want to enable in your portal and for ease of switching between flows as and when needed.

**NOTE:** If you prefer you can create a separate page to render the subscription forms to the different Services. This would be particularly useful if you want to allow multiple applications per account for the *Custom Field & Group Membership Flows*.

How does the 3scale signup work?
--------------------------------
### The Basics
A developer can sign up to subscribe to an API com.microservices.apigateway.security.service in various ways and how they do that is determined by the com.microservices.apigateway.security.configuration of all the settings on the 3scale Admin Portal. Although this is flexible and varying in terms of the process and mechanics working in the background, the end user will not see a great deal of difference from their persepctive and this is key to delivering a good user experience.

Typically the developer would be able to discover any API com.microservices.apigateway.security.service and the differing plans (contracts) as a public user on the homepage of any Developer Portal, when choosing the com.microservices.apigateway.security.service and plan to subscribe to, the necessary parameters to create the contract between the Account and the API com.microservices.apigateway.security.service will be passed to the signup form. There will be an Application created in the background as part of the signup process as this is the object that contains the API keys for consuming the contracted services.

### Objects created during signup
- Account
- User
- Application
- Key(s)

The Account can have many Users but will only have 1 upon initial creation. If the Account has subscribed to multiple Services in the signup then it will be associated to those Services via what's known as a Service subscription and for each Service subscribed an Application will be created along with a set of Keys. Although it is important to understand the 3scale data object com.microservices.apigateway.security.model and the relationship each object has with one another we won't go into too much detail on that here.

### Subscriptions
There are 2 different subscriptions made when the signup is completed. The user commonly only really sees and cares about one of these though. The most important subscription is the Application Subscription and the less important of the two is the Service Subscription.

##### Application Subscription
The Application object has 1:1 relationship to a Service via an Application Plan in 3scale. This is the contract that determines the level of access, rate limits & pricing tier (if it's a monetized API) to consume a single Service.

##### Service Subscription
The Account object can be subscribed to many Services in 3scale and is able to access those Services via the Service Plan -- we use this only to manage the APIs the Account has access to -- as an admin user you will see these subscriptions in the Admin portal as a list of Service Subscriptions.

The Signup Flows
----------------
### Single Application Flow
This is the simplest signup flow that only allows a signup to a single Service and Application Plan upon account creation. No features need to be enabled in the 3scale Developer portal to use this flow. Just include the [single app partial](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/_single_app_signup_form.html.liquid) from your [homepage](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/index.html.liquid) with: `{% include '<PARTIAL_NAME>' %}`.

### Multiple Application Flow
This is a fairly simple signup flow that allows any public users to signup directly to multiple services and the associated Application Plans in just one sign up form. The user does not need an account prior to the signup as we want the Services to be publicly available. The Multiple Applications feature -- *Developer portal > Feature visibility* -- needs to be enabled on the Developer portal to use this flow. Use the [multiple apps partial](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/_mulitple_app_signup_form.html.liquid) and call it from the 
[homepage](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/index.html.liquid) as so: `{% include '<PARTIAL_NAME>' %}`.

### Custom Field Flow
This flow is useful when you want to control the Services a user can see or subscribe to. Imagine a simple scenario where there are 3 categories of APIs that are being managed through 3scale; Public, Private & Internal. The user could be limited to only have the ability to request to subscribe to a Service through some Admin portal settings. However, that then leaves some manual work on the API Provider side to be done every time the user wants to change to another Service or subscribe to a new Service. This isn't easy to maintain if many Services are being published on a frequent basis and with a growing Developer community the Devloper portal needs to remain scalable & manageable.

To use this flow a custom field on the Account object will need to be defined with some predefined options. For example; `public, private, internal`. These values will then be used to perform a substring match using some Liquid logic to render the allowed services and plans for the logged in user. As we want to control the access the user has, the signup form itself will only create an Account and User object. The custom field -- *Settings > Field definitions* -- should be assigned the appropriate value by an admin user once the Account has been created. This part can also be automated using a bit of fancy JavaScript which checks the email domain in the signup form and passes the appropriate custom field parameter value in the signup.

**NOTE:** If you do automate that part it's recommended to enable the "Account approval required" checkbox for added security measures. This can be done at *Settings > General > Signup*. 

The final step is to include the [custom fields partial](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/_custom_field_plans.html.liquid) in the 3scale CMS and call it with the Liquid tag: `{% include '<PARTIAL_NAME>' %}` from the [homepage](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/index.html.liquid).

### Group Membership Flow
This flow is most useful when like in the Custom Fields Flow you want to control the access to Services and in addition you want to create sections of content that can only be accessed by those users with the correct permissions. This flow also requires a user to have signed up to create an account previously. None of the Services or Plans are exposed publicly. The account will be approved by an admin of the API provider team and subsequently assigned the appropriate Group Membership. This will in turn expose the relevant APIs and their plans on the homepage for subscription. To enable this flow a series of steps must be followed.

- The groups should be created under *Developer portal > Groups* the "Allowed sections" can be assigned to their appropriate groups later once they have been created.
- Create a Feature in each of the Services' default Service Plan that denotes the "category" of the API, `public, private, internal` were the examples we used earlier.
- Create a new Section for each Group under *Developer portal > Content* and select "New section" from the dropdown menu. Set the partial path to something that would also be contained as a substring of the Feature `system_name` created previously. It is the string values of these two attributes that will be compared to control the subscription forms the user is presented with.

Finally include the [group membership partial](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/_group_membership_plans.html.liquid) in the 3scale CMS and as with the previous flows call it from the homepage.

Additional points
-----------------

### Demos & Workshops
These templates can be used in conjunction with one another if you're demoing the 3scale Developer Portal and the potential it has. Having the 4 different flows extracted into separate partials makes it very easy to manage and once the test Account & User is set up for demo purposes this should result in a very easy to execute demo.

### JavaScript functions
There is only one custom JavaScript function that you need to use; `redirectUser`. This function redirects the user from the [applications index page](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/_applications_index.html.liquid) to the page defined in the function on the [layout template](https://github.com/kevprice83/Custom-signup-flows-3scale-Dev-portal/blob/master/l_main_layout.html.liquid). When a User first logs in the portal redirects them to the *applications index* view but if they haven't created any Application(s) yet this will feel a bit broken or strange and this custom redirect improves the UX a little.

```
function redirectUser() {
    // This redirect can be set to any page you wish. The default is the homepage.
    window.location.href="/"
};
```

I hope this gist is useful and please leave any comments or suggestions if you have any issues or improvements.

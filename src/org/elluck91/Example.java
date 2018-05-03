package org.elluck91;

import java.net.URI;
import java.util.Arrays;

import com.ciscospark.Membership;
import com.ciscospark.Message;
import com.ciscospark.Person;
import com.ciscospark.Room;
import com.ciscospark.Spark;
import com.ciscospark.Team;
import com.ciscospark.TeamMembership;
import com.ciscospark.Webhook;

class Example {
    public static void main(String[] args) {

        // To obtain a developer access token, visit https://developer.ciscospark.com
        String accessToken = "ZDBmODNjYzUtMGQ1Ni00MjcyLThiNmMtZTVjYTIwNjJhMzU3OGEzZjZhMTctYjQ2";

        // Initialize the client
        Spark spark = Spark.builder()
                .baseUrl(URI.create("https://api.ciscospark.com/v1"))
                .accessToken(accessToken)
                .build();


        // List the rooms that I'm in
        spark.rooms()
                .iterate()
                .forEachRemaining(room -> {
                    System.out.println(room.getTitle() + ", created " + room.getCreated() + ": " + room.getId());
                });


        // Create a new room
        Room room = new Room();
        room.setTitle("Hello World");
        room = spark.rooms().post(room);


        // Add a coworker to the room
        Membership membership = new Membership();
        membership.setRoomId(room.getId());
        membership.setPersonEmail("lukasz.juraszek@sjsu.edu");
        spark.memberships().post(membership);


        // List the members of the room
        spark.memberships()
                .queryParam("roomId", room.getId())
                .iterate()
                .forEachRemaining(member -> {
                    System.out.println(member.getPersonEmail());
                });


        // Post a text message to the room
        Message message = new Message();
        message.setRoomId(room.getId());
        message.setText("Hello World!");
        spark.messages().post(message);


        // Share a file with the room
        message = new Message();
        message.setRoomId(room.getId());
        message.setFiles(URI.create("http://example.com/hello_world.jpg"));
        spark.messages().post(message);


        // Get person details
        Person person=new Person();
        person=spark.people().path("/<<<**Insert PersonId**>>>").get();

        System.out.println("ID - " + person.getId());
        System.out.println("DisplayName - " + person.getDisplayName());
        System.out.println("Emails - " + Arrays.toString(person.getEmails()));
        System.out.println("FirstName - " + person.getFirstName());
        System.out.println("LastName - " + person.getLastName());
        System.out.println("Avatar - " + person.getAvatar());
        System.out.println("OrgID - " + person.getOrgId());
        System.out.println("Roles - " + Arrays.toString(person.getRoles()));
        System.out.println("Licenses - " + Arrays.toString(person.getLicenses()));
        System.out.println("Created - " + person.getCreated());
        System.out.println("TimeZone - " + person.getTimeZone());
        System.out.println("Status - " + person.getStatus());
        System.out.println("Type - " + person.getType());


        // Update avatar
        person.setAvatar("https://developer.ciscospark.com/images/logo_spark_lg@256.png");
        person=spark.people().path("/<<<**Insert PersonId**>>>").put(person);


        // Create a new webhook
        Webhook webhook=new Webhook();
        webhook.setName("My Webhook");
        webhook.setResource("messages");
        webhook.setEvent("created");
        webhook.setFilter("mentionedPeople=me");
        webhook.setSecret("SOMESECRET");
        webhook.setTargetUrl(URI.create("http://www.example.com/webhook"));
        webhook=spark.webhooks().post(webhook);


        // List webhooks
        spark.webhooks().iterate().forEachRemaining(hook -> {
            System.out.println(hook.getId() + ": " + hook.getName() + " (" + hook.getTargetUrl() + ")" + " Secret - " + hook.getSecret());
        });


        // Delete a webhook
        spark.webhooks().path("/<<<**Insert WebhookId**>>>").delete();


        // List people in the organization
        spark.people().iterate().forEachRemaining(ppl -> {
        System.out.println(ppl.getId() + ": " + ppl.getDisplayName()+" : Creation: "+ppl.getCreated());
        });


        // Get organizations
        spark.organizations().iterate().forEachRemaining(org -> {
        System.out.println(org.getId() + ": " + org.getDisplayName()+" : Creation: "+org.getCreated());
        });


        // Get licenses
        spark.licenses().iterate().forEachRemaining(license -> {
        System.out.println("GET Licenses " +license.getId() + ": DisplayName:- " + license.getDisplayName()+" : totalUnits:         "+Integer.toString(license.getTotalUnits())+" : consumedUnits: "+Integer.toString(license.getConsumedUnits()));
        });


        // Get roles
        spark.roles().iterate().forEachRemaining(role -> {
        System.out.println("GET Roles " +role.getId() + ": Name:- " + role.getName());
        });


        // Create a new team
        Team team = new Team();
        team.setName("Brand New Team");
        team = spark.teams().post(team);


        // Add a coworker to the team
        TeamMembership teamMembership = new TeamMembership();
        teamMembership.setTeamId(team.getId());
        teamMembership.setPersonEmail("wile_e_coyote@acme.com");
        spark.teamMemberships().post(teamMembership);


        // List the members of the team
        spark.teamMemberships()
                .queryParam("teamId", team.getId())
                .iterate()
                .forEachRemaining(member -> {
                    System.out.println(member.getPersonEmail());
                });
    }
}

<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

- [Sam and His Snacks Unlimited Online Store](#sam-and-his-snacks-unlimited-online-store)
  - [Storytelling & Visualizing](#storytelling--visualizing)
  - [Designing the System](#designing-the-system)
  - [Demo the Result](#demo-the-result)
  - [Enjoy the Snacks](#enjoy-the-snacks)
  - [Snacks for Everyone](#snacks-for-everyone)
  - [Some Key Discoveries](#some-key-discoveries)

<!-- /code_chunk_output -->


## Sam and His Snacks Unlimited Online Store

...So Sam and Pop embarked on a journey to design and build Snacks Unlimited!!

### Storytelling & Visualizing

Sam and my Team meet to witness Sam's magic, gather the requirements, and transform 
the domain knowledge into an effective online store.

We decide to use two techniques, Domain Storytelling and Event Modeling (ie: blueprinting).

### Designing the System

After our working sessions with Sam, my team has a pretty good idea on what is needed. They are
confident they how it will be done and what patterns to use, but together we want to make sure we come up 
with a system design that is well-architected and can scale to meet demanding snackers' wishes.

We start with building a context map to build out the subdomains, bounded contexts, and integrations between them. - DDD Strategic...

Next we explore using a system design method made popular by the "Righting Software" method. In this exercise we explore decomposing the system around things that frequently change (decomposition by volatility).

Finally, we document our design using the C4 modeling framework to provide a system context view, container view, and various component-level views. 

These methods are useful in "painting the picture" to "better software". Our development team appreciates the visual context of the stories and design that guides them to build modular, quality code.

### Demo the Result

The team is conflicted on whether to use Kafka Streams or FlinkSQL for the Snack Order Processor. It is decided to have a "bake off" and compare the two before choosing which one to use in the final product.

The team, being curious, also wants to compare Kafka vs Redpanda for their Kafka-compatible stream processing engine. So they ensure that the stream processing engine can be swapped out without impacting the snacks unlimited applications.

After a few Sprints, my team is ready to demonstrate our running Snacks Unlimited order mgmt system to Sam. We go through the demo showing some key code snippets the team is proud of as well showing the power of our API-first backend and Order Processing Capabilities.

...NOTE: The Web Frontend team has been working closely with Sam and have developed an awesome user experience for snackers, Sam, and his Snack Delivery Team. Another DEMO has been scheduled to show the complete finished product before "Go Live"...

### Enjoy the Snacks

After seeing the functioning Web UI, API, and Order Processing capabilities ALL is well and we deploy and go live with Snacks Unlimited Online.

The result: Brady, Oakley, and Rylan always get there snacks and are never sad. This makes Mimi & Pop happy AND everyone lives happily ever after.

### Snacks for Everyone

Please grab a snack on your way out. I also have magical Snickers bars for 3 lucky winners. Submit your wish through this Snack Unlimited Promotion. The first 3 customers will get a Snickers :-)

Good luck.

### Some Key Discoveries

* Redpanda had a faster start up time and lighter footprint.
    * No code changes required to swap out Kafka broker with Redpanda "Kafka-compatible" broker.

* Building the Kafka Streams 'snack-order-processor' I had to use the Docker base image `openjdk:17-jdk` instead of `amazoncorretto:17-alpine`. This resulted in a larger image size than the other Spring Boot apps in this demo.
    * This was due to some built in runtime dependencies that Kafka Streams apps required.
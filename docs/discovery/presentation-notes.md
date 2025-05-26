
<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

- [Intro Notes](#intro-notes)
- [Where it Started](#where-it-started)
- [Telling the Story](#telling-the-story)
  - [Sam and His Snacks Unlimited Online Store](#sam-and-his-snacks-unlimited-online-store)
  - [Storytelling & Visualizing](#storytelling--visualizing)
  - [Designing the System](#designing-the-system)
  - [Demo the Result](#demo-the-result)
  - [Snack Promotion WINNERS](#snack-promotion-winners)
  - [Enjoy the Snacks](#enjoy-the-snacks)
  - [Some Key Discoveries](#some-key-discoveries)

<!-- /code_chunk_output -->

## Intro Notes

Hi, my name is Torey Lomenda. I am local to the Twin Cities, and currently am a VP of Solution Architecture & Delivery for Improving. Improving is a modern digital services sofware development, consulting, and training company with 20 offices around the world providing onshore, nearshore, offshore options to our clients (a.k.a. "all-shore consulting services").

Today, I am going to take you on a magical journey OR "at the very least" a practical solution architect's journey on adding context to code to build software that works. I hope to keep this fun while sharing tips & techniques, as well as ways of learning & experimenting I consider beneficial.

I hope you take away from the walkthrough and demo that learning through a journey of "building software that WORKS, through a story you CONNECT with, using solution methods to GUIDE delivery" is worthwhile.

The real world might be "more complex" than what I talk about today, but the methods I highlight, with practice, imagination, and creativity can lead to iteratively delivering real value outcomes in your SDLC (software development lifecycle).

I hope you stick with me on this lighthearted journey to better software :-)


## Where it Started

**A GLIMPSE into how I Learn, Experiment, and Apply Software Methods and Technology**

Before jumping in, I want to take a step back and reflect on where this all started.

Being a consultant, part of the job is keeping an eye on technology trends and applying them to client solutions. So I try and remain curious and  carve out time to play with new technologies and how they can be applied to build better software. 

As a Solution Architect, something that drives my work is finding pragmatic ways of applying domain-driven design (DDD) techniques to build distributed, scalable software systems.

This led me down a learning path, finding ways to work with "domain model aggregates" and related "DDD building blocks" within distributed, event-driven architectures. 

So I stumbled on this nice tutorial with all the tech buzzwords I was looking for. It was a good start - but with many tutorials, it can be easily forgotten after going through it and not applying it. 

However, by adding color with a relatable story and design methods, along with a fully integrated playground "system" running on Docker it has become a powerful learning tool for ongoing experimentation.  

I wanted to share the results of this journey with you today. I have applied many of these techniques and methods throughout the years to various complex, distributed systems with a good level of success. 

## Telling the Story

Our story involves a wizard and keeping children happy with snacks.

Every story you connect with starts with a persona. Relatable characters that you empathize with their needs, goals, and behaviors.

Our journey starts with the semi-fictional story, "Sam and His Snacks Unlimited Online Store".

### Sam and His Snacks Unlimited Online Store

{Read the Story}

...So Sam and Pop embarked on a journey to design and build Snacks Unlimited!!

### Storytelling & Visualizing

Sam and I meet to witness Sam's magic, gather the requirements, and transform the domain knowledge into an effective online store. To get alignment and a solid understanding of what we are tasked to deliver, visually conveying functional requirements is an essential step.

We decide to use two techniques, Domain Storytelling and Event Modeling (ie: blueprinting).

We anchor on the core requirements to understand the TO-BE process to build a System that WORKS.

* "Your wishes are Sam's Commands"
* "Sam needs a penny for your Snacks"

Domain Storytelling is a pictographic language involving actors, work objects, and the activities/interactions between them. This simple, yet powerful method aligns well with domain-driven design building blocks and is great at depicting TO-BE and AS-IS processes at various levels of granularity.

Event Modeling is a way to blueprint a use case flow that visualizes the commands, events, and views on a timeline across system boundaries. This is an approachable method that is well-suited for capturing requirements for a distributed, event-driven system.


### Designing the System

**Paint the PICTURE to Better Software**

After my working sessions with Sam, I have a solid understand of what is needed to build an online store integrated with Sam's magic. Keeping kids happy is high stakes and I am not going to mess this up.

I am  confident how it will be done and what patterns to use, but together we want to make sure we come up with a system design that is well-architected and can scale to meet demanding snackers' wishes.

We start with building out a CONTEXT OVERVIEW that captures our system design and its ability to meet the core use cases/requirements we uncovered during our storytelling sessions. Sam is eager to learn more about designing software systems that works with his magic.

---
We start with building a context map to build out the subdomains, bounded contexts, and integrations between them. This strategic DDD approach is essential to establishing context and clear boundaries.

From Sam's perspective, his magic and "snacks portal" are key differentiators and is "core to his business". The order management subdomain supports Sam. Using magic is my way of keeping inventory and shipping complexities "out-of-scope" to focus on order management. 

From my perspective, the Order Mgmt subdomain is crucial. I want to ensure it scales to meet snackers' wishes amd the lifecycle of a customer order does not impact receiving "wish commands". So I treat it as a "core subdomain" - meaning building custom software tailored to Sam's magic is the "way-to-go" (vs off-the-shelf or a basic CRUD app).

"Context is all about Perspective"

**Partnership (Shared Kernel)**: Two teams collaborate on a small, explicitly agreed-upon subset of the domain model that is shared and jointly maintained.

**Conformist**: The consuming context fully adapts to the model and interface of the supplying context without influencing or negotiating its design.

**Customer/Supplier**: One bounded context (Supplier) provides services or data that another context (Customer) consumes, with coordinated requirements and influence from the customer on the supplier's design.
**Anticorruption Layer (ACL)**: A boundary layer translates and protects the consuming context from the external model of a different context to preserve its own model integrity.

**Open Host Service**: A context exposes its capabilities through a well-defined, open interface that other contexts can use without tight coupling.
**Published Language**: A context communicates through a shared, explicitly defined language or schema (e.g., events or messages) to enable clear and stable integration.
**Separate Ways**: Contexts operate completely independently without integration, usually due to diverging models, priorities, or low integration value.

---

Next we explore using a system design method made popular by the "Righting Software" method. In this exercise I decompose the system around things that frequently change (decomposition by volatility). I know order processing can evolve, and I also know that magic can be volatile and needs to be handled with care ;-)

Sam and I like the resulting "System Design Canvas" view and what it conveys at a high level. By isolating volatility, we can build a cohesive, loosely-coupled system that can withstand change while evolving the architecture.

Sam is satisified with the high level design and leaves me to deliver the work.

---

Before diving straight into code, I REFINE and DOCUMENT the design to help guide the delivery of the system.

I document the design sprinkling in 4+1 UML Views and using the C4 modeling framework, to provide a system context view, container view, and various component-level views I find useful. Each view provides more detail as we drill down from context to code.

As shown in the C4 Container View, I want to compare "like solutions" using Kafka Streams or FlinkSQL for the Snack Order Processor. Since we have a well-designed system, the order processor change is isolated and implementations can be swapped without impacting the rest of the system components.

---

These methods are useful in "painting the picture" to "better software". I have found development teams appreciate the visual context of the stories and design that guides them to build modular, quality code.

Adding "context to code" has helped my continuous learning journey allowing me apply things I learn and build many enhancements beyond a "do and forget" tutorial.

**The tutorial plants the seed, the playground enables growth.**

### Demo the Result

From the design I start coding and integrated within my local Dockerized Playground. During implementation, I also wanted to compare different Kafka-compatible stream processing platforms. 

Whether it is "Confluent Kafka" or "Redpanda" or recently "WarpStream by Confluent", I have verified I can swap out different stream processing engines without impacting the Snacks Unlimitied Applications.

So we have options.

**Redpanda** - written in C++, no Java dependency, offering lower latency and better performance on modern hardware.

**WarpStream** - decouples storage and compute allowing you to "bring your own cloud". It uses object storage like S3 to reduce infrastructure costs and simplifies scaling. WarpStream Agents are "stateless" and no need for "local disks".


After a few Sprints, I am ready to demonstrate a few key outcomes of the Snacks Unlimited order mgmt system to Sam. We go through the demo showing some key code snippets and showing the power of the API-first backend and Order Processing Capabilities.

NOTE: The Web Frontend is the next thing needed to deliver an awesome user experience for snackers, Sam, and his Snack Delivery Team. Another DEMO has been scheduled to show the complete finished product before "Go Live"...

That said, today is our lucky day. Before going "Live", Sam has decided to run a short-term promotion to test out the system and his magic. "YOU HAVE BEEN SELECTED TO BE A PART OF THIS PROMOTION".

Submit your wish through this Snack Unlimited Promotion App. The first four customers will get a Snickers :-)

### Snack Promotion WINNERS

Let's see who the four lucky winners are winners. 

Good luck.

### Enjoy the Snacks

At the end of our journey, everyone lives happily every after. Brady, Oakley, and Rylan always get there snacks and are never sad. Mimi & Pop remain the best grandparents, and Sam is ready to spread snacks and joy around the world!!

### Some Key Discoveries

* Redpanda had a faster start up time and lighter footprint.
    * No code changes required to swap out Kafka broker with Redpanda "Kafka-compatible" broker.

* Adding WarpStream to the Platform was a "snap". 

* Building the Kafka Streams 'snack-order-processor' I had to use the Docker base image `openjdk:17-jdk` instead of `amazoncorretto:17-alpine`. This resulted in a larger image size than the other Spring Boot apps in this demo.
    * This was due to some built in runtime dependencies that Kafka Streams apps required.
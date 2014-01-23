# SMuTPea #

SMuTPea is a Java framework for constructing RFC-compliant Simple Mail Transport Protocol (SMTP) agents.

## Table of Contents ##

- [Why a SMTP framework](#why-a-smtp-framework)
- [Modules](#modules)
- [License](#license)


## Why a SMTP framework ##

There are available numerous SMTP implementations in various programming languages and technologies out there on the Internet. 

Having been asked about how to test an application that sends emails, I was looking for, not a complete SMTP implementation, but a simple MTA that would dump messages rather than send them and that would work without requiring much if any configuration. I didn't locate what I needed, instead finding typically feature-complete SMTP implementations that would work only after substantial configuration effort.

I decided to write what I needed. I read the RFCs for SMTP, and thinking about what I had hoped to find but did not see, what came to mind was a framework that embodied the structure of the SMTP RFC, validating the SMTP dialogue, but giving the implementer free reign as to how to process email passing through.

Thinking about the infrastructure to hand and the way the testing was going to happen, I choose Java and Maven. 


## Modules ##

### SMuTPea-Core ###
*Core* embodies the SMTP RFCs as _Objects_, enforcing the RFC rules, defining the role of the *MTA* while leaving it implementation up to you.
This allows implementors to write their own MTA without having to write the defined dialogues or rules around it, leaving the enforcement of that up to SMutPea-Core.

### SMuTPea-MTA ###
*MTA* provides an abstract MTA that can be extended by the MTA writer. This abstract MTA provides some basic functionality and breaks out the choices the MTA implementor has to consider into well-defined abstract methods.
Also provided here is a _Sink_ MTA: An MTA that looks and acts like an MTA but requires no configuration, no network connection and sends no email. 
Having an MTA that requires not set up and will not send email is useful for testing applications that do expect an MTA to be available.

### SMuTPea-Servers ###
*Servers* provides abstract _servers_ that manage the MTA lifecycle. There are two abstract servers, one that manages the MTA lifecycle on standard _input/output_ streams, and one managing the MTA lifecycle on network _sockets_.
There are also example server implementations that put a _Sink_ server on the command line (allowing the user to practice SMTP interactively themselves) and on TCP sockets.

## License ##

SMuTPea - Copyright 2013 technosf [https://github.com/technosf]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
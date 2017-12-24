# Test task for home24 candidate Software engineer

## General

You are kindly asked to create a recommendation engine from scratch for the
problem described below.

## Architectural background

To be able to manage our articles in an efficient way, we use attributes to
describe all aspects of an article.

## Product background

One of the challenges we’re facing, is finding the right product for our
customers. They might search for something very specific, but in some cases they
might not find it immediately, as we might have a lot of variations of a product
available.

That’s why we’re working on having better product recommendations, to ensure we
can make good proposals to our customers.

## The task

We have extracted some article data and anonymized it. You can find the json
file in the attached tgz file. Each article, identified by its sku, contains a
set of attributes. Each attribute has an assigned value.

Using this data, we want you to build an engine, which can calculate the
similarity of articles. The similarity should be based on the attribute values.
To generate ranking in the engine, we define the following:

An attribute which is higher in the alphabet, has to be weighted heavier, means
att-a has a heavier weight than att-z.

The absolute weight values can be chosen by you, as long as you maintain the
described order.

The expected engine should use one sku as an input parameter, and output the 10
most similar skus including the calculated weights for them.

You can choose whichever language you’re most familiar with. However, we should
be able to run it in our environment, which is probably different from yours.
That's why we'd like you to wrap your application into two bash scripts:

```
init.sh
request.sh
```

## Test task evaluation

Please deliver the solution in a tgz file. The archive should include:
* source code
* `init.sh` script
* `request.sh` script
* tests (optional)

### init.sh

While evaluating your solution we are firstly going to feed the test data to
`init.sh`:

```
$ ./init.sh < home24-test-data.json
```

the `init.sh` prepares the data so that it's efficient to request
recommendations afterwards. For example, `init.sh` can do one of the following:

* launch the web-service which processes test data and load relevancy-index into
  memory.
* process the json file and dump the relevancy-index to file system

It's up to you to decide what `init.sh` does, but the idea is that it does some
preprocessing, so that all of the next requests for recommendations are
efficient.

### request.sh

Then we are going to use the `request.sh` script to get the list of most
relevant articles:

```
$ ./request.sh sku-1
```
request.sh should print a list of 10 most relevant skus and their weights to
stdout:

```
sku-2,140
sku-10,109
sku-3,106
sku-4,103
sku-15,30
sku-36,30
sku-27,10
sku-108,9
sku-39,9
sku-310,8
```

the list should be ordered by relevance. The most relevant articles should be on
top.

## Feedback

We’re always trying to improve our tests. We would appreciate your feedback on
the following questions:

* How did you like the test?
* How much time did you spend on it?
* Do you think the amount of time is ok for a test task within an interview
  process?
* If you could ask us to change one thing on the test, what would it be?

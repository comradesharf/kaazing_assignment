'use strict';

function handleException(e) {
    console.error(e);
}

var NewsTicker = React.createClass({
    getInitialState: function () {
        return {connection: void 0, session: void 0, consumer: void 0, headlines: ["No Headline"]};
    },
    componentWillMount: function () {
        var instance = this;
        if (this.state.connection == null) {
            var jmsConnectionFactory = new JmsConnectionFactory('ws://localhost:8001/jms');
            try {
                var connectionFuture = jmsConnectionFactory.createConnection(null, null, function () {
                    if (!connectionFuture.exception) {
                        try {
                            var connection = connectionFuture.getValue();
                            connection.setExceptionListener = function (e) {
                                handleException(e);
                            };
                            connection.start(function () {
                                var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                                instance.setState({connection: connection, session: session});
                            });
                        } catch (e) {
                            handleException(e);
                        }
                    } else {
                        handleException(connectionFuture.exception);
                    }
                });
            } catch (e) {
                handleException(e);
            }
        }
    },
    handleClick: function (topicName) {
        var instance = this;
        alert("You have subsribed to " + topicName);

        if (instance.state.consumer) {
            instance.state.consumer.close(null);
        }

        var session = this.state.session;
        var topic = session.createTopic("/topic/" + topicName);
        var consumer = session.createConsumer(topic);
        this.setState({headlines: [], consumer: consumer});

        consumer.setMessageListener(function (message) {
            instance.state.headlines.push(message.getText());
            instance.setState({headlines: instance.state.headlines});
        });
    },
    handleSportNews: function () {
        this.handleClick("sports")
    },
    handleWorldNews: function () {
        this.handleClick("world")
    },
    handleStopNews: function () {
        if (this.state.consumer) {
            this.state.consumer.close(null);
            this.setState({headlines: []});
        }
    },
    render: function () {
        return (
            <div>
                <button className="btn btn-primary" onClick={this.handleSportNews}>Get Sports News</button>
                <button className="btn btn-primary" onClick={this.handleWorldNews}>Get World News</button>
                <button className="btn btn-link" onClick={this.handleStopNews}>Stop All News Subscription</button>
                <table className="table table-bordered">
                    <tbody>
                        {
                            this.state.headlines.map(function (headline) {
                                return (
                                    <Headline headline={headline}/>
                                );
                            })
                            }
                    </tbody>
                </table>
            </div>
        );
    }
});

var Headline = React.createClass({
        render: function () {
            return (
                <tr>
                    <td>{this.props.headline}</td>
                </tr>
            );
        }
    }
);

jQuery(function () {
    React.render(
        <NewsTicker/>,
        document.getElementById("content")
    )
});
    

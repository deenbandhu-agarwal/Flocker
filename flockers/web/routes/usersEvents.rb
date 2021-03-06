require_relative '../helpers/init.rb'

module Flockers
  class WebApp < Sinatra::Application

    get '/users/events' do
      authenticate!

      user = Account.first(:id => session['userid'].to_i)
      events = user.events

      content_type :json
      events.to_json
    end

    post '/users/events' do
      authenticate!
      event = params[:event];
      puts session['userid']
      #begin 
          Event.createEvent({ 
             :ename => event[:ename], 
             :date => event[:date],
             :time => event[:time],
             :place => event[:place],
             :organizer => session['user'],
             :account_id => session['userid'],
             :fees => event[:fees],
             :prize => event[:prize],
             :description =>event[:description],
             :verb => event[:verb],
             :activity =>event[:activity]})

       #rescue => e
          #response = {:error => {:message => e.message}}
       #end
       content_type :json
       response.to_json
       
    end

    put '/users/events' do
       begin 
          authenticate!
          puts params
          response = {}
          event = Event.first(:id => params[:event].to_i)
          account = Account.first(:id => session['userid'].to_i)

          event.addAttendee(account)
       rescue => e
          response = {:error => {:message => e.message}}
       end

       content_type :json
       response.to_json
    end

    delete '/users/events' do
      raise "Incorrect Arguments" if params[:event_id].nil?
      event = Event.first(:id => params[:event_id].to_i);
      par = Participation.all(:event_id => params[:event_id].to_i);
      par.destroy
      event.destroy
      # Only the organizer can do this
      # Not allowed right now.. need to figure out policy to let the other users know
    end

    #
    #  This is to GET / POST participation to an event
    #
    get '/users/events/participant' do
      authenticate!

      user = Account.first(:id => session['userid'].to_i)
      events = user.participatedEvents

      content_type :json
      events.to_json
    end

    post '/users/events/participant' do
       begin 
          authenticate!
          response = {}
          event = Event.first(:id => params[:event].to_i)
          account = Account.first(:id => session['userid'].to_i)
          raise "Owner of a flock is by default a participant!" if account.id == event.account_id
          event.addAttendee(account)
       rescue => e
          response = {:error => {:message => e.message}}
       end
       content_type :json
       response.to_json
    end

    delete '/users/events/participant' do
      raise "Incorrect Arguments" if params[:event_id].nil?
      authenticate!
      par = Participation.first(:event_id => params[:event_id].to_i , :account_id => session['userid']);
      par.destroy
    end

end
end

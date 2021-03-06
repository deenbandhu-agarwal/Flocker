require 'data_mapper'

class Event
end

class Participation
end

class Verb
end

class Activity
end

class Account 
  include DataMapper::Resource

  property :id, Serial
  property :created_at,   DateTime, :default => Time.now
  property :uname, String, :required => true
  property :password, String, :required => true

  has n, :participations
  has n, :participatedEvents, :model => Event, :child_key => [:id], :parent_key => [:event_id], :through => :participations

  has n, :events

  # NOTE: this should not be used right now.. use createEvent to create event 
  def addEvent(a)
    self.save
    self.events.reload
    self.events << a unless a.nil?
    return self.events.save!
  end

  def self.create_account args
    raise "You must create an account with an email address" if args[:uname].nil?

    new_account = Account.create()

    new_account.uname = args[:uname] 
    new_account.password = args[:password] if args.has_key? :password
    new_account.created_at = Time.now   
    new_account.save
    raise 'couldnt create account; wont save' unless new_account.saved?

    if new_account.saved? then new_account else raise 'cannot create account' end
  end

end

class Event
  include DataMapper::Resource

  property :id,     Serial
  property :created_at,   DateTime, :default => Time.now
  property :ename, String, :required => true
  property :date, Date
  property :time, Time
  property :place, String
  property :organizer, String
  property :fees,Integer
  property :prize, Integer
  property :description,String
  property :verb,String
  property :activity,String

  has n, :participations
  has n, :attendees, :model => Account, :child_key => [:id], :parent_key => [:account_id], :through => :participations

  belongs_to :account

  def self.createEvent args
    raise 401, "Must provide owner" if args[:account_id].nil?

    newEvent = Event.create :account => Account.first(:id => args[:account_id])
    newEvent.account.save
    newEvent.account.reload

    newEvent.ename = args[:ename]
    newEvent.date = args[:date] if args.has_key? :date
    newEvent.time = args[:time] if args.has_key? :time
    newEvent.place = args[:place] if args.has_key? :place
    newEvent.organizer = args[:organizer] if args.has_key? :organizer
    newEvent.fees = args[:fees].to_i if args.has_key? :fees
    newEvent.prize = args[:prize].to_i if args.has_key? :prize
    newEvent.description = args[:description] if args.has_key? :description
    newEvent.verb = args[:verb] if args.has_key? :verb
    newEvent.activity = args[:activity] if args.has_key? :activity

    newEvent.save

    if newEvent.saved?
      newEvent
    else
      newEvent.errors.each do |e|
        puts "error: #{e}\n"
      end

      raise 'could not save new event in Event.createEvent'
    end
  end

  def addAttendee(a)
    self.save
    self.attendees.reload
    self.attendees << a unless a.nil?
    return self.attendees.save!
  end

end

class Participation
  include DataMapper::Resource
  property :created_at,   DateTime, :default => Time.now

  belongs_to :participatedEvent, :model => Event, :key => true, :child_key => [:event_id]
  belongs_to :attendee, :model => Account, :key => true, :child_key => [:account_id]
end

class Verb
  include DataMapper::Resource
  property :id,           Serial
  property :created_at,   DateTime, :default => Time.now
  property :verb,         String, :unique => true

  has n,   :activities, :model => Activity
end

class Activity
  include DataMapper::Resource
  property :id,           Serial
  property :created_at,   DateTime, :default => Time.now
  property :activity,     String, :unique => true

  belongs_to :verb, :model => Verb
end

# this is very cricial otherwise you get the 
#  undefined method `include?' for nil:NilClass:
DataMapper.finalize

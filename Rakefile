require 'rubocop/rake_task'
require 'rspec/core/rake_task'

RSpec::Core::RakeTask.new(:spec)
RuboCop::RakeTask.new(:inspect) do |task|
  task.options = ['-crubocop.yml']
end

task default: :inspect

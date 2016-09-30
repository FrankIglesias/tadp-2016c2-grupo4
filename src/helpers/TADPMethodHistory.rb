class TADPMethodHistory
  attr_accessor :method, :params

  def initialize (method, *params)
    self.method= method
    self.params = *params
  end

  def se_llamo (symbol, *params)
    if params.length==0
      self.method ==symbol
    else
      (self.method == symbol) && (self.params.eql? (params))
    end
  end
end

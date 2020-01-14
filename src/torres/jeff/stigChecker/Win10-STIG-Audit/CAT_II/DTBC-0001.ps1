## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-44711"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Google Chrome Current Windows STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Google\Chrome").DefaultSearchProviderSearchURL
if ($Regkey -eq 'https://google.com/#q={searchTerm}'){
    $Configuration = 'Completed'}
    elseif($Regkey -eq 'https://www.bing.com/search?q={searchTerms} '){
        $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit